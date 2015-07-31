package com.app.laundry.tabs.lib;

public interface IconPagerAdapter2 {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}
