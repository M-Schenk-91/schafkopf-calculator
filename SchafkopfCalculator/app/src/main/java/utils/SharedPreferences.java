package utils;

import android.content.Context;

public class SharedPreferences {
   private static SharedPreferences instance;
   private static Context context;

   public SharedPreferences(Context context) {
      this.context = context;
   }

   public static SharedPreferences getInstance(Context context){
      if(instance == null) instance = new SharedPreferences(context);
      return instance;
   }
}
