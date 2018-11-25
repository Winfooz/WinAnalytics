package com.winfooz;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public interface Destroyable {

    void destroy();

    Destroyable EMPTY_DESTROYABLE = () -> {};
}
