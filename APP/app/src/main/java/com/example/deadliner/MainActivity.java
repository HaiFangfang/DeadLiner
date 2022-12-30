package com.example.deadliner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final String[] menus = {"日程", "任务", "统计"};
    private final int[] menuLogos = {R.drawable.one, R.drawable.one, R.drawable.one};

    private List<Fragment> fragmentList;
    private ViewPager2 vpMain;
    private TabLayout tlMain;

    private static MyDBhelper dbHelper;
//    private TabLayout.Tab tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        dbHelper=new MyDBhelper(this);

        initView(); // 初始化控件

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new TaskFragment());
        fragmentList.add(new StatFragment());

        // 创建ViewPager2所使用的适配器，FragmentStateAdapter抽象类的实现类对象
        FragmentStateAdapter adapter = new FragmentStateAdapter(MainActivity.this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        vpMain.setAdapter(adapter); // 给ViewPager2设置适配器

        new TabLayoutMediator(tlMain, vpMain, false, false, (tab, position) -> { // TabLayout和ViewPager2关联到一起

            tab.setCustomView(tabMenu(menuLogos[position], menus[position])); // 设置Tab的图标和标题
        }).attach(); // 调用该方法才能真正绑定起来
    }

    private void initView() {
        vpMain = findViewById(R.id.vpMain);
        tlMain = findViewById(R.id.tlMain);
    }

    private View tabMenu(int image, String title) {
        ImageView ivMenu;
        TextView tvMenu;
        View view = LayoutInflater.from(this).inflate(R.layout.tabmenu, null);
        ivMenu = view.findViewById(R.id.ivMenu);
        tvMenu = view.findViewById(R.id.tvMenu);
        tvMenu.setText(title);
        ivMenu.setImageResource(image);
        return view;
    }
    public static MyDBhelper getdb(){
        return dbHelper;
    }
    public List<Fragment> getFragmentList(){
        return fragmentList;
    }
}
