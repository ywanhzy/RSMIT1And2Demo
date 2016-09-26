package com.example.rsmit1and2demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.suntech.rsmit.RSMIT;
import com.suntech.rsmit.RSMIT2;

import java.util.HashMap;
//import android.widget.Toast;


public class MainActivity extends Activity
{
	
	private Spinner selectType_Sp;
	private String[] RSMITSELECT = { "MIT", "MIT2" };
	private int SPFLAG = 0;
	private Button playBtn;
	private Button stopBtn;
	//MIT
	private RelativeLayout MIT_RL;
	private EditText MIT_Edt;
	private boolean SENDFLAG = false;
	private boolean LISTENFLAG = false;
	//MIT2
	private RelativeLayout MIT2_RL;
	private EditText Demo_Error_Edt;
	private EditText Demo_Title_Edt;
	private EditText Demo_Action_Edt;
	private EditText Demo_Value1_Edt;
	private EditText Demo_Value2_Edt;	
	private EditText Demo_Group_Edt;
	private EditText Demo_Type_Edt;
	
	private Thread recordThread;
	private Thread playbackThread;
	private static RSMIT mit;
	private static RSMIT2 mit2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewsLink();
		initViews();
		
	}
	
	
	private void viewsLink()
	{
		selectType_Sp = (Spinner) findViewById(R.id.selectType_Sp);
		playBtn = (Button) findViewById(R.id.playBtn);
		stopBtn = (Button) findViewById(R.id.stopBtn);
		MIT_RL = (RelativeLayout) findViewById(R.id.MIT_RL);
		MIT_Edt = (EditText) findViewById(R.id.MIT_Edt);
		MIT2_RL = (RelativeLayout) findViewById(R.id.MIT2_RL);
		Demo_Error_Edt = (EditText) findViewById(R.id.Demo_Error_Edt);
		Demo_Title_Edt = (EditText) findViewById(R.id.Demo_Title_Edt);
		Demo_Action_Edt = (EditText) findViewById(R.id.Demo_Action_Edt);
		Demo_Value1_Edt = (EditText) findViewById(R.id.Demo_Value1_Edt);
		Demo_Value2_Edt = (EditText) findViewById(R.id.Demo_Value2_Edt);
		Demo_Group_Edt = (EditText) findViewById(R.id.Demo_Group_Edt);
		Demo_Type_Edt = (EditText) findViewById(R.id.Demo_Type_Edt);
	}
	
	
	private void initViews()
	{
//		////
//		selectType_Sp.setEnabled(false);
		
		ArrayAdapter<String> Type = new ArrayAdapter<String>(
				MainActivity.this, android.R.layout.simple_spinner_item, RSMITSELECT);
		selectType_Sp.setAdapter(Type);
		selectType_Sp.setOnItemSelectedListener( new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				SPFLAG = position;
				switch (position)
				{
					//MIT
					case 0:
						MIT_RL.setVisibility(View.VISIBLE);
						MIT2_RL.setVisibility(View.GONE);
						playBtn.setText("Play");
						stopBtn.setText("Listen");
						MIT_Edt.setText("");
						break;
						
					//MIT2
					case 1:
						MIT_RL.setVisibility(View.GONE);
						MIT2_RL.setVisibility(View.VISIBLE);
						playBtn.setText("Receive");
						stopBtn.setText("Stop");
						Demo_Error_Edt.setText("");
						Demo_Action_Edt.setText("");
						Demo_Title_Edt.setText("");
						Demo_Value1_Edt.setText("");
						Demo_Value2_Edt.setText("");
						Demo_Group_Edt.setText("");
						Demo_Type_Edt.setText("");
						break;
					
					default:
						break;
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
			}
		} );
		
		playBtn.setOnClickListener(isClicked);
		playBtn.setText("Play");
		stopBtn.setOnClickListener(isClicked);
		stopBtn.setText("Listen");
		
		Demo_Error_Edt.setFocusable(false);
		Demo_Error_Edt.setClickable(false);
		Demo_Title_Edt.setFocusable(false);
		Demo_Title_Edt.setClickable(false);
		Demo_Action_Edt.setFocusable(false);
		Demo_Action_Edt.setClickable(false);
		Demo_Value1_Edt.setFocusable(false);
		Demo_Value1_Edt.setClickable(false);
		Demo_Value2_Edt.setFocusable(false);
		Demo_Value2_Edt.setClickable(false);
		Demo_Group_Edt.setFocusable(false);
		Demo_Group_Edt.setClickable(false);
		Demo_Type_Edt.setFocusable(false);
		Demo_Type_Edt.setClickable(false);
	}
	
	
	private OnClickListener isClicked = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch (v.getId())
			{
				case R.id.playBtn:
					if( SPFLAG == 0 )
					{
						//MIT
						SENDFLAG = !SENDFLAG;
						if( SENDFLAG )
						{
							MITSend();
						}
						else{
							SENDStop();
						}			
						
						MITSetView(playBtn, SENDFLAG);
						
					}
					else if( SPFLAG == 1 )
					{
						//MIT2
						MIT2Receive();
						MIT2SetView(true);
					}					
					
					break;
					
				case R.id.stopBtn:
					if( SPFLAG == 0 )
					{
						//MIT
						LISTENFLAG = !LISTENFLAG;
						if( LISTENFLAG )
						{
							MIT_Edt.setText("");
							MITListen();
						}
						else{
							LISTENStop();
						}	
						
						MITSetView(stopBtn, LISTENFLAG);
						
					}
					else if( SPFLAG == 1 )
					{
						//MIT2
						MIT2Stop();
						MIT2SetView(false);
					}
					
					break;
				
				default:
					break;
			}
			
		}
	};
	
	
//===== MIT ===============================	
	private void MITSetView( View view, boolean flag )
	{
		switch (view.getId())
		{
			case R.id.playBtn:
				if( flag )
				{
					playBtn.setText("Stop");
					stopBtn.setEnabled(false);
					MIT_Edt.setEnabled(false);
				}
				else {
					playBtn.setText("Play");
					stopBtn.setEnabled(true);
					MIT_Edt.setEnabled(true);
				}
				
				break;
				
			case R.id.stopBtn:
				if( flag )
				{
					stopBtn.setText("Stop");
					playBtn.setEnabled(false);
					MIT_Edt.setEnabled(false);
				}
				else {
					stopBtn.setText("Listen");
					playBtn.setEnabled(true);
					MIT_Edt.setEnabled(true);
				}
				break;
			
			default:
				break;
		}
		
	}
	
	
	private void MITSend()
	{
		playbackThread = new Thread(){

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				String send = MIT_Edt.getText().toString();
				mit = new RSMIT( MainActivity.this );
				mit.send(send);
				runOnUiThread( new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						
					}
				} );
			}
			
		};
		playbackThread.start();
		
	}
	
	
	private void MITListen()
	{
		recordThread = new Thread(){

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				mit = new RSMIT( MainActivity.this );				
				final String result = mit.receive();
				
				if( result != null )
				{
					runOnUiThread( new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub							
							MIT_Edt.setText(result);
							LISTENFLAG = !LISTENFLAG;
							MITSetView(stopBtn, LISTENFLAG);
							LISTENStop();
						}
					} );
				}
				else {
					runOnUiThread( new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							
						}
					} );
				}
				
			}
						
		};		
		recordThread.start();
		
	}
	
	
	private void SENDStop()
	{
		if( mit != null )
		{
			mit.interruptSend();
			try
			{				
				if( playbackThread != null )
				{
					playbackThread.interrupt();
					playbackThread.join();
				}
				
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void LISTENStop()
	{
		if( mit != null )
		{		
			mit.interruptReceive();
			try
			{
				if( recordThread != null )
				{
					recordThread.interrupt();
					recordThread.join();
				}
				
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
//===== MIT2 ===============================	
	private void MIT2SetView( boolean flag )
	{
		if( flag )
		{
			playBtn.setEnabled(false);
			Demo_Error_Edt.setEnabled(false);
			Demo_Title_Edt.setEnabled(false);
			Demo_Action_Edt.setEnabled(false);
			Demo_Value1_Edt.setEnabled(false);
			Demo_Value2_Edt.setEnabled(false);
			Demo_Error_Edt.setText("");
			Demo_Title_Edt.setText("");
			Demo_Action_Edt.setText("");
			Demo_Value1_Edt.setText("");	
			Demo_Value2_Edt.setText("");
			Demo_Group_Edt.setText("");
			Demo_Type_Edt.setText("");
		}
		else {
			playBtn.setEnabled(true);
			Demo_Error_Edt.setEnabled(true);
			Demo_Title_Edt.setEnabled(true);
			Demo_Action_Edt.setEnabled(true);
			Demo_Value1_Edt.setEnabled(true);
			Demo_Value2_Edt.setEnabled(true);
			Demo_Group_Edt.setEnabled(true);
			Demo_Type_Edt.setEnabled(true);
		}
	}
	
	
	private void MIT2Receive()
	{
		recordThread = new Thread(){

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				mit2 = new RSMIT2(MainActivity.this);
				final HashMap<String, String> result = mit2.receive();
				if( result != null )
				{
					runOnUiThread( new Runnable()
					{
						
						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							if( mit2.errorMessage != null )
							{
								Demo_Error_Edt.setText(mit2.errorMessage);
							}
							else {
								Demo_Error_Edt.setText(result.get("errorMessage"));
							}
							
							Demo_Title_Edt.setText(result.get("title"));
							Demo_Action_Edt.setText(result.get("action"));
							Demo_Value1_Edt.setText(result.get("value1"));
							Demo_Value2_Edt.setText(result.get("value2"));
							Demo_Group_Edt.setText(result.get("group"));
							Demo_Type_Edt.setText(result.get("type"));
							MIT2Stop();
							MIT2SetView(false);
							
						}
					} );
				}
				else {
					if( mit2.errorMessage != null )
					{
						runOnUiThread( new Runnable()
						{
							
							@Override
							public void run()
							{
								// TODO Auto-generated method stub
								Demo_Error_Edt.setText(mit2.errorMessage);
								MIT2Stop();
								MIT2SetView(false);
							}
						} );
					}
				}

			}
			
		};
		recordThread.start();
	}
	
	
	private void MIT2Stop()
	{
		if( mit2 != null )

		{
			mit2.interruptReceive();
			try
			{
				
				if( recordThread != null )
				{
					recordThread.interrupt();
					recordThread.join();
				}
				
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
