package com.syc.sequence.service;

import com.syc.sequence.entity.Sequence;
import com.syc.sequence.mapper.SequenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class SequenceService {
    @Autowired
    private SequenceMapper sequenceMapper;
    @Autowired
    private SequenceService sequenceService;
    /**
     * 号段大小
     * */
    private final int allocateSize = 1000;
    /**
     * key - sequenceType value - 号段起始值
     * */
    private final Map<String, Long> allocateMaps = new ConcurrentHashMap<>();//当前号段
    /**
     * key - sequenceType value - 号段内自增值
     * */
    private final Map<String, AtomicLong> incrementMaps = new ConcurrentHashMap<>(); //业务号段当前值
    /**
     * 进程锁
     * */
    private final ReentrantLock lock = new ReentrantLock();//锁

    public long next(String sequenceType) {
        //用进程锁，这样每个服务实例就会用新的号段
        lock.lock();
        try {
            if (allocateMaps.containsKey(sequenceType) && incrementMaps.get(sequenceType).incrementAndGet() < allocateSize) {
                return allocateMaps.get(sequenceType) + incrementMaps.get(sequenceType).longValue();
            }
            return sequenceService.nextValues(sequenceType,1);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param count 递增间隔
     * */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long nextValues(String sequenceType,int count) {
        Sequence sequence = sequenceMapper.getForUpdate(sequenceType);
        if (sequence == null) {
            sequence = new Sequence();
            sequence.setCrtTime(LocalDateTime.now());
            sequence.setSequenceType(sequenceType);
            sequence.setStartValue(1);
            sequence.setCurrValue(1);
            try {
                sequenceMapper.insert(sequence);
            } catch (Exception e) {
                // Duplicated conflict
                sequence = sequenceMapper.getForUpdate(sequenceType);
                if (sequence == null) {
                    throw new RuntimeException("Unable init sequence, sequenceType=[" + sequenceType + "].");
                }
            }
        }
        long seqValue = sequence.getCurrValue();
        long value = seqValue;
        while (value >= 0 && Long.MAX_VALUE - value < count) {
            // 序列值循环: 当value + count 大于 0Long.MAX_VALUE时，从startValue重新开始累加
            count -= (int) (Long.MAX_VALUE - value + 1);
            value = sequence.getStartValue();
        }
        sequence.setCurrValue(value + count + allocateSize); // nextValue
        sequenceMapper.updateById(sequence);
        // currValue 大于 allocateMaps 一个号段值
        allocateMaps.put(sequenceType, value + count);
        incrementMaps.put(sequenceType, new AtomicLong(0));
        return seqValue;
    }
}
