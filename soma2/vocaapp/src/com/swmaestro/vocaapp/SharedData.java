package com.swmaestro.vocaapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedData 
{

	private Context m_ct;
	private SharedPreferences m_pref;
	private SharedPreferences.Editor m_editor;
	
	public SharedData( Context ct ) 
	{
		this.m_ct = ct ;
		this.m_pref = m_ct.getSharedPreferences( "SharedData", Context.MODE_PRIVATE ) ;
	}

	protected void editMode( )
	{
		this.m_editor = this.m_pref.edit( ) ;
	}
	protected void editCommit( )
	{
		this.m_editor.commit( ) ;
	}
	
	/// boolean ///
	public boolean getBoolean( String key ) 
	{
		return this.m_pref.getBoolean( key, false ) ;
	}

	public void setBoolean( String key, boolean val ) 
	{
		editMode( ) ;
		this.m_editor.putBoolean( key, val ) ;
		editCommit( ) ;
	}

	/// string ///
	public String getString( String key ) 
	{
		return this.m_pref.getString( key, "" ) ;
	}
	
	/// string ///
	public String getString( String key, String defaultValue ) 
	{
		return this.m_pref.getString( key, defaultValue ) ;
	}

	public void setString( String key, String val ) 
	{
		editMode( ) ;
		this.m_editor.putString( key, val ) ;
		editCommit( ) ;
	}
	
	/// int ///
	public int getInt( String key ) 
	{
		return this.m_pref.getInt( key, 0 ) ;
	}
	
	public int getInt( String key, int defaultValue ) 
	{
		return this.m_pref.getInt( key, defaultValue ) ;
	}

	public void setInt( String key, int val ) 
	{
		editMode( ) ;
		this.m_editor.putInt( key, val ) ;
		editCommit( ) ;
	}
	//////////////
	
	/// float ///
	public float getFloat( String key ) 
	{
		return this.m_pref.getFloat( key, 0.0f ) ;
	}

	public void setFloat( String key, float val ) 
	{
		editMode( ) ;
		this.m_editor.putFloat( key, val ) ;
		editCommit( ) ;
	}
	//////////////
	
	/// double 더블형은 지원 안해서 스트링으로 바꾼다  
	/// 어짜피 빠른 작업을 하기엔 그닥이니 이렇게 해도 별 상관 없지 않을까 한다. 
	public double getDouble( String key ) 
	{
		try 
		{
			double val = Double.parseDouble( this.m_pref.getString( key, "0.0" ) ) ;
			return val ;
		} catch (Exception e) 
		{
			return 0.0 ;
		}
	}

	public void setDouble( String key, float val ) 
	{
		editMode( ) ;
		this.m_editor.putString( key, "" + val ) ;
		editCommit( ) ;
	}
	//////////////
}
