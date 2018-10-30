package io.javalovers.seq;

import io.javalovers.exception.SequenceException;

public interface SequenceDao {

    long getNextSequenceId(String key) throws SequenceException;

}