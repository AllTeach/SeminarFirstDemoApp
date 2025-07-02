# Android Room Tutorial (with Threads, Step-by-Step)

--- 

## **Step 1: Add Room Dependencies**

### **Theory**
Room provides a modern, type-safe abstraction over SQLite.  
You must add its dependencies to use it in your project.

### **Hands-On**
- Open your module’s `build.gradle` (`app/build.gradle`).
- Add:

  ```gradle
  implementation "androidx.room:room-runtime:2.6.1"
  annotationProcessor "androidx.room:room-compiler:2.6.1"
  ```

- Sync your project.

---

## **Step 2: Define the Entity**

### **Theory**
- An **Entity** is a class representing a database table.
- Each field is a column.
- `@Entity` marks the class as a table.
- `@PrimaryKey` marks the primary key.
- `@ColumnInfo` (optional) customizes the column name.

### **Hands-On**
- **Create a new class:**  
  Right-click your Java/Kotlin package → New → Java Class → Name it `FruitItem`.

- Paste:

  ```java name=FruitItem.java
  import androidx.room.Entity;
  import androidx.room.PrimaryKey;

  @Entity
  public class FruitItem {
      @PrimaryKey(autoGenerate = true)
      private int id;

      private String name;
      private int imageResId;
      private String description;

  }
  ```

  *(You can add `@ColumnInfo(name = "...")` if you want to customize column names, but it's optional.)*

---

## **Step 3: Create the DAO (Data Access Object)**

### **Theory**
- A **DAO** is an interface with annotated methods for accessing the database.
- Room generates the implementation for you.

### **Hands-On**
- **Create a new interface:**  
  Right-click your package → New → Java Interface → Name it `FruitItemDao`.

- Paste:

  ```java name=FruitItemDao.java
  import androidx.room.Dao;
  import androidx.room.Insert;
  import androidx.room.Query;
  import androidx.room.Delete;
  import java.util.List;

  @Dao
  public interface FruitItemDao {
      @Insert
      void insert(FruitItem item);

      @Query("SELECT * FROM FruitItem")
      List<FruitItem> getAll();

      @Delete
      void delete(FruitItem item);
  }
  ```

---

## **Step 4: Create the Database Class (Singleton Pattern)**

### **Theory**
- The **Database** class is an abstract class extending `RoomDatabase`.
- It ties together your DAOs and entities, and Room generates the implementation.
- **Singleton Pattern:**  
  You should only ever have one instance of your Room database open in your app.  
  The singleton pattern ensures this, preventing memory leaks, crashes, and ensuring data consistency.

### **Hands-On**
- **Create a new class:**  
  Right-click your package → New → Java Class → Name it `FruitDatabase`.

- Paste:

  ```java name=FruitDatabase.java
  import android.content.Context;
  import androidx.room.Database;
  import androidx.room.Room;
  import androidx.room.RoomDatabase;

  @Database(entities = {FruitItem.class}, version = 1)
  public abstract class FruitDatabase extends RoomDatabase {
      private static volatile FruitDatabase INSTANCE;

      public abstract FruitItemDao fruitItemDao();

      // Singleton pattern for database instance
      public static FruitDatabase getDatabase(final Context context) {
          if (INSTANCE == null) {
              synchronized (FruitDatabase.class) {
                  if (INSTANCE == null) {
                      INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                              FruitDatabase.class, "fruit_database")
                              .build();
                  }
              }
          }
          return INSTANCE;
      }
  }

  
  ```

**Explanation:**
- `private static volatile FruitDatabase INSTANCE;`  
  Holds the single instance of the database.
- `getDatabase(Context context)`  
  Returns the singleton instance, creating it if necessary, in a thread-safe way.
- Always use `FruitDatabase.getDatabase(context)` to get your database object, never call `Room.databaseBuilder` directly in activities or threads.

---

## **Step 5: Use Room in Your Activity (with Threads, Direct DAO Calls, Singleton)**

### **Theory**
- Room does not allow DB operations on the main (UI) thread.
- Use a `Thread` for DB work, and `runOnUiThread()` for UI updates.
- Always use the singleton pattern to access your database.

### **Hands-On**
- In your activity (`FruitActivity` or similar):

  ```java name=FruitActivity.java
  import android.os.Bundle;
  import androidx.appcompat.app.AppCompatActivity;
  import java.util.List;

  public class FruitActivity extends AppCompatActivity {
      List<FruitItem> fruitList;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_fruit);

          // Load all fruits (in a background thread)
          new Thread(() -> {
              FruitItemDao dao = FruitDatabase.getDatabase(this).fruitItemDao();
              List<FruitItem> fruits = dao.getAll();
              runOnUiThread(() -> {
                  fruitList.clear();
                  fruitList.addAll(fruits);
                  // Update UI here (e.g., adapter.notifyDataSetChanged())
              });
          }).start();

          // Insert a fruit (in a background thread)
          FruitItem item = new FruitItem();
          item.name = "Banana";
          item.imageResId = R.drawable.banana;
          new Thread(() -> {
              FruitItemDao dao = FruitDatabase.getDatabase(this).fruitItemDao();
              dao.insert(item);
              List<FruitItem> fruits = dao.getAll();
              runOnUiThread(() -> {
                  fruitList.clear();
                  fruitList.addAll(fruits);
                  // Update UI here
              });
          }).start();

          // Delete a fruit (in a background thread, e.g., after a user action)
          
          new Thread(() -> {
              FruitItemDao dao = FruitDatabase.getDatabase(this).fruitItemDao();
              dao.delete(fruitList.get(0));
              List<FruitItem> fruits = dao.getAll();
              runOnUiThread(() -> {
                  fruitList.clear();
                  fruitList.addAll(fruits);
                  // Update UI here
              });
          }).start();
          
      }
  }
  ```

---

## **Step 6: Add a Repository Layer (Recommended, Singleton Pattern)**

### **Theory**
- A **Repository** abstracts the data layer from the UI and handles threading for you.
- This makes your activity code simpler and more maintainable.
- The Repository should use the singleton database instance.

### **Hands-On**
- **Create a new class:**  
  Right-click your package → New → Java Class → Name it `FruitRepository`.

- Paste:

  ```java name=FruitRepository.java
  import android.content.Context;
  import java.util.List;

  public class FruitRepository {
      private final FruitItemDao dao;

      public FruitRepository(Context context) {
          this.dao = FruitDatabase.getDatabase(context).fruitItemDao();
      }

      public void getAll(FruitCallback<List<FruitItem>> callback) {
          new Thread(() -> {
              List<FruitItem> list = dao.getAll();
              callback.onResult(list);
          }).start();
      }

      public void insert(FruitItem item, FruitCallback<List<FruitItem>> callback) {
          new Thread(() -> {
              dao.insert(item);
              List<FruitItem> list = dao.getAll();
              callback.onResult(list);
          }).start();
      }

      public void delete(FruitItem item, FruitCallback<List<FruitItem>> callback) {
          new Thread(() -> {
              dao.delete(item);
              List<FruitItem> list = dao.getAll();
              callback.onResult(list);
          }).start();
      }

      public interface FruitCallback<T> {
          void onResult(T result);
      }
  }
  ```

---

### **Usage from Activity (with Repository, using Singleton DB)**

```java name=FruitActivity.java
// ... inside onCreate, after initializing repo:
FruitRepository repo = new FruitRepository(this);
/*
// Load all fruits
repo.getAll(list -> runOnUiThread(() -> {
    fruitList.clear();
    fruitList.addAll(list);
    // Update UI here
}));

// Insert a fruit
FruitItem item = new FruitItem();
item.name = "Banana";
item.imageResId = R.drawable.banana;
repo.insert(item, list -> runOnUiThread(() -> {
    fruitList.clear();
    fruitList.addAll(list);
    // Update UI here
}));

// Delete a fruit
// repo.delete(fruitList.get(0), list -> runOnUiThread(() -> {
//     fruitList.clear();
//     fruitList.addAll(list);
//     // Update UI here
// }));

 */
```

---

## **Summary Table**

| Step | Theory                                      | Hands-On (What to Create/Do)                                     |
|------|---------------------------------------------|------------------------------------------------------------------|
| 1    | Add Room dependencies                       | Edit `build.gradle`                                              |
| 2    | Entity: Table schema                        | **Create new class:** `FruitItem.java`                           |
| 3    | DAO: Data access methods                    | **Create new interface:** `FruitItemDao.java`                    |
| 4    | Database: Main RoomDatabase (Singleton)     | **Create new class:** `FruitDatabase.java` (with singleton)      |
| 5    | Use DB in Activity (Threads & UI updates)   | Use singleton DB in your activity                                |
| 6    | Repository (simplifies UI logic, optional)  | **Create new class:** `FruitRepository.java`, use in activity    |

---

## **Why Use the Singleton Pattern for Room Database?**

- **Performance:** Creating a Room database is expensive. Singleton ensures only one instance is created and reused.
- **Consistency:** All DAOs and threads interact with the same database instance, preventing data inconsistency.
- **Safety:** Avoids memory leaks and possible crashes from having multiple open instances.
- **Best Practice:** Recommended in official Android documentation.

---
