package com.example.android.persistence;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.example.android.persistence.model.Product;

/**
 * -au
 * notice that we're extending LifecycleActivity (which extends FragmentActivity)
 * and not AppCompatActivity (which also extends FragmentActivity)
 * It means we don't have embedded Toolbar support.
 * In release 1.0 of LifecycleActivity it will extend AppCompatActivity.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            ProductListFragment fragment = new ProductListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, ProductListFragment.TAG).commit();
        }

        // -au
        // we can create components here that will depend on LifecycleRegistry
        // using getLifecycle()
        // see ProductListFragment for a useage example

    }

    /** Shows the product detail fragment */
    public void show(Product product) {

        ProductFragment productFragment = ProductFragment.forProduct(product.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("product")
                .replace(R.id.fragment_container,
                        productFragment, null).commit();
    }
}
