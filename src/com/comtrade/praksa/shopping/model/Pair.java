package com.comtrade.praksa.shopping.model;

// TODO: Auto-generated Javadoc
/**
 * The Class Pair. Simple pair class
 *
 * @param <F> the generic type
 * @param <S> the generic type
 */
public class Pair<F, S> {
	
	/** The first. */
	private F first;
	
	/** The second. */
	private S second;

	/**
	 * Creates the.
	 *
	 * @param <F> the generic type
	 * @param <S> the generic type
	 * @param first the first
	 * @param second the second
	 * @return the pair
	 */
	public static <F, S> Pair<F, S> create(F first, S second) {
		return new Pair<F, S>(first, second);
	}
	
	/**
	 * Instantiates a new pair.
	 *
	 * @param first the first
	 * @param second the second
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public F getFirst() {
		return first;
	}

	/**
	 * Sets the first.
	 *
	 * @param first the new first
	 */
	public void setFirst(F first) {
		this.first = first;
	}

	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public S getSecond() {
		return second;
	}

	/**
	 * Sets the second.
	 *
	 * @param second the new second
	 */
	public void setSecond(S second) {
		this.second = second;
	}

}
