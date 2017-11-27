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

package com.example.android.persistence.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.example.android.persistence.db.DatabaseCreator;
import com.example.android.persistence.db.entity.TodoTaskEntity;

import java.util.List;

public class TodoTaskListViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<List<TodoTaskEntity>> mObservableTodoTasks;

    public TodoTaskListViewModel(Application application) {
        super(application);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        LiveData<Boolean> databaseCreated = databaseCreator.isDatabaseCreated();
        mObservableTodoTasks = Transformations.switchMap(databaseCreated,
                new Function<Boolean, LiveData<List<TodoTaskEntity>>>() {
            @Override
            public LiveData<List<TodoTaskEntity>> apply(Boolean isDbCreated) {
                Log.w("databaseCreated changed", isDbCreated.toString());
                if (!Boolean.TRUE.equals(isDbCreated)) { // Not needed here, but watch out for null
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                    LiveData <List<TodoTaskEntity>> todoTaskEntity = databaseCreator.getDatabase().todoTaskDao().loadAllTodoTasks();
                    return todoTaskEntity;
                }
            }
        });

        databaseCreator.createDb(this.getApplication());
    }

    /**
     * Expose the LiveData TodoTasks query so the UI can observe it.
     */
    public LiveData<List<TodoTaskEntity>> getTodoTasks() {
        return mObservableTodoTasks;
    }
}
