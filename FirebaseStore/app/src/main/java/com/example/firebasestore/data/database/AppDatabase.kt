package com.example.firebasestore.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.firebasestore.data.dao.CartDao
import com.example.firebasestore.data.dao.CartProductDao
import com.example.firebasestore.data.dao.CartUserDao
import com.example.firebasestore.data.dao.CategoryDao
import com.example.firebasestore.data.dao.ProductDao
import com.example.firebasestore.data.dao.ProductImageDao
import com.example.firebasestore.data.dao.UserDao
import com.example.firebasestore.data.entity.Cart
import com.example.firebasestore.data.entity.CartProduct
import com.example.firebasestore.data.entity.CartUser
import com.example.firebasestore.data.entity.Category
import com.example.firebasestore.data.entity.Product
import com.example.firebasestore.data.entity.ProductImage
import com.example.firebasestore.data.entity.User

@Database(
    entities = [Cart::class, CartProduct::class, CartUser::class,
        Category::class, Product::class, ProductImage::class,
        User::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun cartProductDao(): CartProductDao
    abstract fun cartUserDao(): CartUserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun productImageDao(): ProductImageDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}