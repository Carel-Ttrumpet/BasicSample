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

import com.example.android.persistence.db.entity.TodoTaskEntity;

import java.util.ArrayList;
import java.util.List;

/** Generates dummy data and inserts them into the database */
class DatabaseInitUtil {

    static void initializeDb(AppDatabase db) {
        List<TodoTaskEntity> todoTasks = new ArrayList<>(3);
        generateData(todoTasks);
        insertData(db, todoTasks);
    }

    private static void generateData(List<TodoTaskEntity> todoTasks) {

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

    private static void insertData(AppDatabase db, List<TodoTaskEntity> todoTasks ) {
        db.beginTransaction();
        try {
            db.todoTaskDao().insertAll(todoTasks);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
