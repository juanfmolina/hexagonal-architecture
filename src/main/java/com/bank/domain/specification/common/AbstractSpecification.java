package com.bank.domain.specification.common;

/**
 * Abstract class defined in the Specification Pattern: @see <a href="https://en.wikipedia.org/wiki/Specification_pattern">Specification Pattern Wiki</a>
 *
 * @param <T> Typed class used fot whatever class where need to evaluate the specification
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

    public abstract boolean isSatisfiedBy(T t);

}
