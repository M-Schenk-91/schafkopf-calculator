package ui.activities;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.schenk.matthias.schafkopfcalculator.R;

import game.Game;
import game.GameController;
import game.GameSettings;
import game.Statistics;
import io.IOManager;
import io.SaveGameUpdater;
import ui.AppRater;
import ui.FragmentController;
import ui.custom.SchafkopfActivity;
import ui.fragments.GameSetupFragment;
import ui.fragments.StatisticsFragment;
import ui.fragments.dialog.LoadGameDialogFragment;
import ui.fragments.dialog.SaveGameDialogFragment;
import ui.interfaces.IGameListener;
import ui.interfaces.IGameSettingsFragmentListener;

public class MainActivity extends SchafkopfActivity
      implements IGameSettingsFragmentListener, IGameListener {

   private FragmentController mgrFragments;
   private GameController mgrGame;
   private IOManager mgrIO;
   //private DrawerLayout drawerLayout;
   private BottomNavigationView navigationView;
   private Toolbar toolbar;
   private ActionBarDrawerToggle drawerToggle;
   private boolean paused = false;
   private SaveGameUpdater saveGameUpdater;
   private boolean cachedGameUpdated = false;
   private TextView toolbarTitle;
   private ImageView toolBarImage;

   @Override
   protected void onCreate(Bundle savedInstanceState) {

      setTheme(R.style.AppTheme);

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      findControls();
      init();
      initUpdates();
      listeners();

      AppRater.getInstance(this)
            .app_launched();
   }

   private void init() {
      mgrGame = GameController.getInstance();
      mgrFragments = new FragmentController(this);
      mgrIO = new IOManager(this);

      setSupportActionBar(toolbar);
      toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      //drawerToggle = setupDrawerToggle();
      //drawerLayout.addDrawerListener(drawerToggle);

      mgrFragments.setFragment(FragmentController.FRAGMENT_GAME_SETUP, false);
      setToolBarTitle(getResources().getString(R.string.title_new_game), getResources().getDrawable(R.drawable.ic_heart));

      MenuItem itemSetup = navigationView.getMenu()
            .getItem(FragmentController.FRAGMENT_GAME_SETUP);
      setMenuItemChecked(itemSetup);

      MenuItem itemGame = navigationView.getMenu()
            .getItem(FragmentController.FRAGMENT_GAME);
      itemGame.setEnabled(gameAvailable);

      MenuItem itemStatistics = navigationView.getMenu()
            .getItem(FragmentController.FRAGMENT_STATISTICS);
      itemStatistics.setEnabled(gameAvailable);
   }

   private void initUpdates() {
      saveGameUpdater = SaveGameUpdater.getInstance();
      saveGameUpdater.setGameModeUpdate(mgrGame.getHmAvailableModes());
   }

   @Override
   protected void onPause() {
      paused = true;
      super.onPause();
   }

   @Override
   protected void onStop() {
      paused = false;
      if (gameAvailable) {
         cacheActiveGame();
      }
      super.onStop();
   }

   @Override
   protected void onResume() {
      super.onResume();
      resetInactivityTimer();

      if (!paused) {
         loadCachedGame();
      }

      paused = false;
   }

   public void onBackPressed () {
      finish();
   }

   private void loadCachedGame() {
      loadGame(true);
   }

   private void cacheActiveGame() {
      mgrIO.saveGame(GameController.getActiveGame(),
            IOManager.NAME_DIRECTORY_CURRENTLY_CACHED_GAME);
   }

   private void listeners() {
      mgrGame.addGameListener(this);
      mgrIO.addGameListener(this);

      mgrGame.addGameListener(Statistics.INSTANCE);
      mgrIO.addGameListener(Statistics.INSTANCE);

      Statistics.INSTANCE.addStatisticsListener(
            (StatisticsFragment) mgrFragments.getFragment(FragmentController.FRAGMENT_STATISTICS));

      ((GameSetupFragment) mgrFragments.getFragment(
            FragmentController.FRAGMENT_GAME_SETUP)).addGameSettingsListener(this);

      navigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(MenuItem menuItem) {
                  switch (menuItem.getItemId()) {
                     case R.id.new_game:
                        mgrFragments.setFragment(FragmentController.FRAGMENT_GAME_SETUP, true);
                        setToolBarTitle(getResources().getString(R.string.title_new_game), getResources().getDrawable(R.drawable.ic_heart));
                        break;
                     case R.id.game_overwiew:
                        mgrFragments.setFragment(FragmentController.FRAGMENT_GAME, true);
                        setToolBarTitle(getResources().getString(R.string.title_game), getResources().getDrawable(R.drawable.ic_ball));
                        break;
                     case R.id.statistics:
                        mgrFragments.setFragment(FragmentController.FRAGMENT_STATISTICS, true);
                        setToolBarTitle(getResources().getString(R.string.title_statistics), getResources().getDrawable(R.drawable.ic_leaf));
                        break;
                  }

                  setMenuItemChecked(menuItem);
                  return true;
               }
            });
   }

   private void setToolBarTitle(String text, Drawable ic) {
      toolbarTitle.setText(text);
      toolBarImage.setImageDrawable(ic);
   }

   private void findControls() {
      //drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      navigationView = (BottomNavigationView) findViewById(R.id.navigation);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitle = findViewById(R.id.toolbar_text);
      toolBarImage = findViewById(R.id.toolbar_logo);
   }

   @Override
   public void onCreateNewGame(GameSettings settings) {
      mgrGame.createNewGame(settings);
   }

   @Override
   public void onGameCreated(Game game, boolean newGame, boolean cached, String loadingMessage) {

      if (game == null) {
         showToast(getResources().getString(R.string.loadingError) + ": " + loadingMessage);
         return;
      }

      if (cached) {
         cachedGameUpdated = true;
      }

      gameAvailable = true;
      mgrGame.setActiveGame(game);
      switchToGameFragment(cached);

      if (!newGame && !cached) {
         showToast(getResources().getString(R.string.loaded));
      }

      mgrFragments.updateFragments(game);
   }

   private void switchToGameFragment(boolean cached) {
      mgrFragments.setFragment(FragmentController.FRAGMENT_GAME, !cached);

      MenuItem item = navigationView.getMenu()
            .getItem(FragmentController.FRAGMENT_GAME);
      item.setEnabled(gameAvailable);

      MenuItem itemStatistics = navigationView.getMenu()
            .getItem(FragmentController.FRAGMENT_STATISTICS);
      itemStatistics.setEnabled(gameAvailable);

      setToolBarTitle(getResources().getString(R.string.title_game), getResources().getDrawable(R.drawable.ic_ball));
      setMenuItemChecked(item);
   }

   @Override
   public void onGameRoundsChanged(Game game) {
   }

   @Override
   public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);
      /*
      if (hasFocus) {
         getWindow().getDecorView()
               .setSystemUiVisibility(
                     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
      }
      */
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      /*
      if (drawerToggle.onOptionsItemSelected(item)) {
         return true;
      }
*/
      switch (item.getItemId()) {
         case R.id.action_save:
            if (gameAvailable) {
               saveCurrentGame();
            } else {
               showToast(getResources().getString(R.string.no_game_available));
            }
            return true;

         case R.id.action_load:
            loadGame(false);
            return true;
      }

      return super.onOptionsItemSelected(item);
   }

   private void loadGame(boolean cachedGame) {
      if (cachedGame) {
         mgrIO.loadGame(IOManager.NAME_DIRECTORY_CURRENTLY_CACHED_GAME, !cachedGameUpdated);
      } else {
         if (mgrIO.savedGamesExisting()) {
            LoadGameDialogFragment loadGameDialogFragment = new LoadGameDialogFragment();
            loadGameDialogFragment.show(getSupportFragmentManager(), "load");
         } else {
            showToast(getResources().getString(R.string.no_games_saved));
         }
      }
   }

   private void saveCurrentGame() {
      SaveGameDialogFragment saveGameDialogFragment = new SaveGameDialogFragment();
      saveGameDialogFragment.show(getSupportFragmentManager(), "save");
   }
/*
   private ActionBarDrawerToggle setupDrawerToggle() {
      return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
            R.string.drawer_close);
   }

   @Override
   protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      drawerToggle.syncState();
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      drawerToggle.onConfigurationChanged(newConfig);
   }
*/
   public void setMenuItemChecked(MenuItem menuItem) {
      //uncheck all
      int size = navigationView.getMenu()
            .size();
      for (int i = 0; i < size; i++) {
         navigationView.getMenu()
               .getItem(i)
               .setChecked(false);
      }

      menuItem.setChecked(true);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.settings, menu);
      return true;
   }
}
