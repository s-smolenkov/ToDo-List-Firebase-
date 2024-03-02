package com.example.todolistfirebase

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://todo-list-dee49-default-rtdb.europe-west1.firebasedatabase.app/")

    @Singleton
    @Provides
    fun provideToDoRepository(database: FirebaseDatabase): ToDoRepository = ToDoRepository(database)
}