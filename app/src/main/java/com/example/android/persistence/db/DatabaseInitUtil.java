/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.db;

import android.util.Log;

import com.example.android.persistence.db.entity.CommentEntity;
import com.example.android.persistence.db.entity.ProductEntity;
import com.example.android.persistence.db.entity.TodoTaskEntity;
import com.example.android.persistence.model.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/** Generates dummy data and inserts them into the database */
class DatabaseInitUtil {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stanman",
            "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] COMMENTS = new String[]{
            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6",
    };

    static void initializeDb(AppDatabase db) {
        List<ProductEntity> products = new ArrayList<>(FIRST.length * SECOND.length);
        List<CommentEntity> comments = new ArrayList<>();
        List<TodoTaskEntity> todoTasks = new ArrayList<>(3);

        generateData(products, comments, todoTasks);

        insertData(db, products, comments, todoTasks);
    }

    private static void generateData(List<ProductEntity> products, List<CommentEntity> comments, List<TodoTaskEntity> todoTasks) {
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                ProductEntity product = new ProductEntity();
                product.setName(FIRST[i] + " " + SECOND[j]);
                product.setDescription(product.getName() + " " + DESCRIPTION[j]);
                product.setPrice(rnd.nextInt(240));
                product.setId(FIRST.length * i + j + 1);
                products.add(product);
            }
        }

        for (Product product : products) {
            int commentsNumber = rnd.nextInt(5) + 1;
            for (int i = 0; i < commentsNumber; i++) {
                CommentEntity comment = new CommentEntity();
                comment.setProductId(product.getId());
                comment.setText(COMMENTS[i] + " for " + product.getName());
                comment.setPostedAt(new Date(System.currentTimeMillis()
                        - TimeUnit.DAYS.toMillis(commentsNumber - i) + TimeUnit.HOURS.toMillis(i)));
                comments.add(comment);
            }
        }

        TodoTaskEntity todoTask1 = new TodoTaskEntity();
        todoTask1.setDescription("Learn Android LifeCycleComponents");
        todoTask1.setCompleted(true);
        todoTasks.add(todoTask1);

        TodoTaskEntity todoTask2 = new TodoTaskEntity();
        todoTask2.setDescription("Learn Fragments");
        todoTask2.setCompleted(false);
        todoTasks.add(todoTask2);

        TodoTaskEntity todoTask3 = new TodoTaskEntity();
        todoTask3.setDescription("Learn Data binding");
        todoTask3.setCompleted(false);
        todoTasks.add(todoTask3);

    }

    private static void insertData(AppDatabase db, List<ProductEntity> products, List<CommentEntity> comments, List<TodoTaskEntity> todoTasks ) {
        db.beginTransaction();
        try {
            db.productDao().insertAll(products);
            db.commentDao().insertAll(comments);
            db.todoTaskDao().insertAll(todoTasks);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
