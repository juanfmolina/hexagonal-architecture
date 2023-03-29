package com.bank.domain.specification.common;

/**
 * Interface defined in the Specification Pattern: @see <a href="https://en.wikipedia.org/wiki/Specification_pattern">Specification Pattern Wiki</a>
 * @param <T> Typed class used fot whatever class where need to evaluate the specification
 */
public interface Specification<T> {

    boolean isSatisfiedBy(T t);

}
