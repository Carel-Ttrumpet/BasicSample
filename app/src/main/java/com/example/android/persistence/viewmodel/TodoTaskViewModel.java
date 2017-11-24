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
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.android.persistence.db.DatabaseCreator;
import com.example.android.persistence.db.entity.TodoTaskEntity;


public class TodoTaskViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<TodoTaskEntity> mObservableTodoTask;

    public ObservableField<TodoTaskEntity> todo = new ObservableField<>();

    private final int mTodoTaskId;

  

    public TodoTaskViewModel(@NonNull Application application, final int todoId) {
        super(application);
        mTodoTaskId = todoId;

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());


        mObservableTodoTask = Transformations.switchMap(databaseCreator.isDatabaseCreated(), new Function<Boolean, LiveData<TodoTaskEntity>>() {
            @Override
            public LiveData<TodoTaskEntity> apply(Boolean isDbCreated) {
                if (!isDbCreated) {
                    //noinspection unchecked
                    return ABSENT;
                } else {
                    //noinspection ConstantConditions
                    return databaseCreator.getDatabase().todoTaskDao().loadTodoTask(mTodoTaskId);
                }
            }
        });

        databaseCreator.createDb(this.getApplication());

    }
    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    public LiveData<TodoTaskEntity> getObservableTodoTask() {
        return mObservableTodoTask;
    }

    public void setTodoTask(TodoTaskEntity todo) {
        this.todo.set(todo);
    }

    /**
     * A creator is used to inject the todo ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the todo ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mTodoTaskId;

        public Factory(@NonNull Application application, int todoId) {
            mApplication = application;
            mTodoTaskId = todoId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TodoTaskViewModel(mApplication, mTodoTaskId);
        }
    }
}
