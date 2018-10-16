package com.example.gray_dog3.applist;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Process;
import java.util.ArrayList;
import java.util.List;
import android.widget.*;
import java.util.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.net.*;
import java.io.*;
import android.content.pm.PackageManager.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.icu.text.*;
import android.view.*;

public class MainActivity extends AppCompatActivity
{
    private ListView lv_app_list;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();
	int total;
	private ProgressBar mPro;
	Context context;
	String packName,appName,appVer;
	String appBasePath ;

	LinearLayout layout;
	TextView et,ett;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		setTitle("正在读取应用...");
		mPro = (ProgressBar)findViewById(R.id.activitymainProgressBar1);
		lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);

		context = getApplicationContext();


		initAppList();

		//新建文件夹
		newFo();
		
		
		

		lv_app_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View view, int p3, long p4)
				{
					// TODO: Implement this method
					TextView et = (TextView) view.findViewById(R.id.tv_app_name);// 从layout中获得控件,根据其id
					packName = et.getText().toString();
					appName = getAppName(packName);
					appVer = getAppVersion(packName);

					AlertDialog alertDialog = new AlertDialog
						.Builder(MainActivity.this)	
						.setTitle(appName + " " + appVer)
						.setItems(new String[]{"终结进程","冻结","解冻","清除数据", "卸载"}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								int item = which + 1;

								if (item == 1)
								{
String a[]={"am force-stop "+packName};
new shell().execCommand(a,true);
								}
								else if (item == 2)
								{
									String a[]={"am force-stop "+packName};
									new shell().execCommand(a,true);
								}
								else if (item == 3)
								{
									String a[]={"pm disable "+packName};
									new shell().execCommand(a,true);
								}
								else if (item == 4)
								{
									String a[]={"pm enable "+packName};
									new shell().execCommand(a,true);
								}
								else if(item==5){
									String a[]={"pm uninstall "+packName};
									new shell().execCommand(a,true);
								}
							}
						}).create();
					alertDialog.show();


					return true;
				}


			});




		lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{

					//	LinearLayout layout = (LinearLayout)lv_app_list.getChildAt(position);// 获得子item的layout
					TextView et = (TextView) view.findViewById(R.id.tv_app_name);// 从layout中获得控件,根据其id
					packName = et.getText().toString();
					appName = getAppName(packName);
					appVer = getAppVersion(packName);
					//Toast.makeText(getApplicationContext(),appName,1).show();
					//Toast.makeText(MainActivity.this, packName, Toast.LENGTH_SHORT).show();

					AlertDialog alertDialog = new AlertDialog
						.Builder(MainActivity.this)	
						.setTitle(appName + " " + appVer)

						.setItems(new String[]{"打开","详情","发送", "提取"}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								int item = which + 1;

								if (item == 1)
								{
									try
									{
										openapp(packName);
									}
									catch (Exception e)
									{
										openAlert("错误代码", e + "");
									}
								}
								else if (item == 2)
								{
									try
									{
										startSetting(packName);
									}
									catch (Exception e)
									{
										openAlert("错误代码", e + "");
									}
								}
								else if (item == 3)
								{
									mPro.setVisibility(0);
									new Thread() {
										@Override
										public void run()
										{
											super.run();
											//新线程操作
											sendApp(packName);
											runOnUiThread(new Runnable(){
													@Override
													public void run()
													{
														//更新UI操作
														mPro.setVisibility(8);

														Toast.makeText(getApplicationContext(), "调用发送程序", 1).show();

													}});}}.start();
								}
								else if (item == 4)
								{
									mPro.setVisibility(0);
									new Thread() {
										@Override
										public void run()
										{
											super.run();
											//新线程操作
											getApp(packName);
											runOnUiThread(new Runnable(){
													@Override
													public void run()
													{
														//更新UI操作
														mPro.setVisibility(8);	
														File a = new File("/sdcard/应用清单缓存/应用清单的提取缓存/" + appName + "_" + appVer + ".apk");

														openAlert("提取完成", "App:" + appName + "\nSize:" + getDataSize(a.length()) + "\nHash:" + a.hashCode()+"\n原路径:"+appBasePath+"\n提取到:"+a.getAbsolutePath());
													}});}}.start();
								}
							}
						}).create();
					alertDialog.show();
				}
			});
    }

	
	//菜单 
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.sd:
sdAlert();
				break;
			case R.id.about:
startActivity(new Intent(MainActivity.this,AboutActivity.class));
				break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	
	public void sdAlert(){
		
		View view=(LinearLayout) getLayoutInflater().inflate(R.layout.sdalert,null);  
		TextView a1 = (TextView)view.findViewById(R.id.sdalertTextView1);
		TextView a2 = (TextView)view.findViewById(R.id.sdalertTextView2);
		TextView a3 = (TextView)view.findViewById(R.id.sdalertTextView3);
		ProgressBar p1 = (ProgressBar)view.findViewById(R.id.sdalertProgressBar1);
		ProgressBar p2 = (ProgressBar)view.findViewById(R.id.sdalertProgressBar2);
		ProgressBar p3 = (ProgressBar)view.findViewById(R.id.sdalertProgressBar3);
		
		//sd
		String a = new SDutil().getFreeSpace();
		String b = new SDutil().getTotalExternalMemorySize();
		//Toast.makeText(getApplicationContext(),""+c,1).show();
		float c = Float.parseFloat(a)/Float.parseFloat(b);
		DecimalFormat df = new DecimalFormat("##.##%");
		a3.setText("sdcard"+"-"+df.format(c));
		p3.setProgress((int)(c*100));
		//sys
		String f = new SDutil().getSystemFree();
		String g = new SDutil().getSystemTotal();
		//Toast.makeText(getApplicationContext(),""+c,1).show();
		float h = Float.parseFloat(f)/Float.parseFloat(g);
		DecimalFormat dg = new DecimalFormat("##.##%");
		a2.setText("system"+"-"+dg.format(h));
		p2.setProgress((int)(h*100));
		//
		String q = new SDutil().getDataFree();
		String w = new SDutil().getDataTotal();
		//Toast.makeText(getApplicationContext(),""+c,1).show();
		float e = Float.parseFloat(q)/Float.parseFloat(w);
		DecimalFormat dh = new DecimalFormat("##.##%");
		a1.setText("data"+"-"+dh.format(e));
		p1.setProgress((int)(e*100));
		
		AlertDialog.Builder sd = new AlertDialog.Builder(this);
		
		sd.setTitle("储存情况");
		sd.setView(view);
		sd.setCancelable(false);
		sd.setPositiveButton("了解",null);
		sd.show();
		
		
		}
	
	
	
	
	public static String getDataSize(long size)
	{
        if (size < 0)
		{
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024)
		{
            return size + "bytes";
        }
		else if (size < 1024 * 1024)
		{
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        }
		else if (size < 1024 * 1024 * 1024)
		{
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        }
		else if (size < 1024 * 1024 * 1024 * 1024)
		{
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        }
		else
		{
            return "size: error";
        }

    }

	
	
	
	
	

	//获取APP名
	public String getAppName(String packageName)
	{  

		PackageManager pm = context.getPackageManager();    
		String Name ;    
		try
		{     
			Name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();    
		}
		catch (PackageManager.NameNotFoundException e)
		{     
			Name = "" ;   
		}   
		return Name
			;}
//获取版本
	public String getAppVersion(String packname)
	{
		//包管理操作管理类
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo packinfo = pm.getPackageInfo(packname, 0);
			return packinfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return packname;
	}

//新建文件夹
	public void newFo()
	{
		File destDir = new File("/sdcard/应用清单缓存/");
		if (!destDir.exists())
		{
			destDir.mkdirs();
		}

		File destDi = new File("/sdcard/应用清单缓存/应用清单的发送缓存/");

		if (!destDi.exists())
		{
			destDi.mkdirs();
		}

		File destD = new File("/sdcard/应用清单缓存/应用清单的提取缓存/");
		if (!destD.exists())
		{
			destD.mkdirs();
		}

		deleteFile(new File("/sdcard/应用清单缓存/应用清单的发送缓存/"));
	}

	//删除文件
	private void deleteFile(File file)
	{
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				File f = files[i];
				deleteFile(f);
			}
			//file.delete();//如要保留文件夹，只删除文件，请注释这行
		}
		else if (file.exists())
		{
			file.delete();
		}
	}


	//分享APP
	public void sendApp(String packn)
	{

		try
		{
			File file = new File(getPackageManager().getApplicationInfo(packn, 0).sourceDir);
			appBasePath = file.getAbsolutePath();
			copyFile(appBasePath, "/sdcard/应用清单缓存/应用清单的发送缓存/" + appName + "_" + appVer + ".apk");
			分享("/sdcard/应用清单缓存/应用清单的发送缓存/" + appName + "_" + appVer + ".apk");
			//Toast.makeText(getApplicationContext(),"分享成功",1).show();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			openAlert("分享失败", e + "");
		}
	}

	//提取APP
	public void getApp(String packn)
	{



		try
		{

			File file = new File(getPackageManager().getApplicationInfo(packn, 0).sourceDir);
			appBasePath = file.getAbsolutePath();
			copyFile(appBasePath, "/sdcard/应用清单缓存/应用清单的提取缓存/" + appName + "_" + appVer + ".apk");
			//Toast.makeText(getApplicationContext(),"提取成功",1).show();

		}
		catch (PackageManager.NameNotFoundException e)
		{}
	}

//复制
	public void copyFile(String oldPath, String newPath)
	{   
		try
		{   
			int bytesum = 0;   
			int byteread = 0;   
			File oldfile = new File(oldPath);   
			if (oldfile.exists())
			{ //文件存在时   
				InputStream inStream = new FileInputStream(oldPath); //读入原文件   
				FileOutputStream fs = new FileOutputStream(newPath);   
				byte[] buffer = new byte[1444];   
				int length;   
				while ((byteread = inStream.read(buffer)) != -1)
				{   
					bytesum += byteread; //字节数 文件大小   
					System.out.println(bytesum);   
					fs.write(buffer, 0, byteread);   
				}   
				inStream.close();   
			}   
		}   
		catch (Exception e)
		{   
			System.out.println("复制单个文件操作出错");   
			e.printStackTrace();   

		}   

	}   


	void 分享(String path)
	{
		Intent imageIntent = new Intent(Intent.ACTION_SEND);
		imageIntent.setType("*/*");
		imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.format("file://%s", path)));
		startActivity(Intent.createChooser(imageIntent, "分享"));

	}





	public void openAlert(String title, String message)
	{
		new AlertDialog.Builder(this).setTitle(title).setMessage(message).show();
	}

//启动APP
	public void openapp(String packn)
	{
		Intent resolveIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packn);
		getApplicationContext().startActivity(resolveIntent);

	}
//app.设置
	public void startSetting(String packn)
	{
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9)
		{
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packn, null));
        }
		else if (Build.VERSION.SDK_INT <= 8)
		{
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packn);
        }
        startActivity(intent);
    }

//刷新列表
    private void initAppList()
	{
        new Thread(){
            @Override
            public void run()
			{
                super.run();
                //扫描得到APP列表
                final List<MyAppInfo> appInfos = ApkTool.scanLocalInstallAppList(MainActivity.this.getPackageManager());
                total = appInfos.size();
				mHandler.post(new Runnable() {
						@Override
						public void run()
						{
							mAppAdapter.setData(appInfos);
							setTitle("应用数量:" + total);
							mPro.setVisibility(8);

						}
					});
            }
        }.start();
    }



    class AppAdapter extends BaseAdapter
	{

        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();

        public void setData(List<MyAppInfo> myAppInfos)
		{
            this.myAppInfos = myAppInfos;
            notifyDataSetChanged();
        }

        public List<MyAppInfo> getData()
		{
            return myAppInfos;
        }

        @Override
        public int getCount()
		{
            if (myAppInfos != null && myAppInfos.size() > 0)
			{
                return myAppInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position)
		{
            if (myAppInfos != null && myAppInfos.size() > 0)
			{
                return myAppInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position)
		{
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
		{
            ViewHolder mViewHolder;
            MyAppInfo myAppInfo = myAppInfos.get(position);
            if (convertView == null)
			{
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            }
			else
			{
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
            return convertView;
        }

        class ViewHolder
		{

            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }


}
