/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @filename ShakeListener.java
 * @package cn.edu.csu.iteliter.listener
 * @project TinyWeibo 微微博
 * @description 手机摇晃的监听器
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:58:49
 * @version 1.0
 * 
 */
public class ShakeListener implements SensorEventListener {

	/** 加速度的阈值 */
	private static final int SPEED_SHRESHOLD = 3000;

	/** 更新间隔时间 */
	private static final int UPTATE_INTERVAL_TIME = 70;

	/** 传感器管理器 */
	private SensorManager sensorManager;

	/** 传感器 */
	private Sensor sensor;

	/** 摇晃监听器 */
	private OnShakeListener onShakeListener;

	/** Context */
	private Context mContext;

	/** 最新的x坐标 */
	private float lastX;

	/** 最新的y坐标 */
	private float lastY;

	/** 最新的z坐标 */
	private float lastZ;

	/** 最后的更新时间 */
	private long lastUpdateTime;

	/**
	 * 构造器
	 * 
	 * @param c
	 *            Context
	 */
	public ShakeListener(Context c) {
		mContext = c;
		start();
	}

	/**
	 * 开始监听
	 */
	public void start() {
		sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager != null) {
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		if (sensor != null) {
			sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	/**
	 * 停止监听
	 */
	public void stop() {
		sensorManager.unregisterListener(this);
	}

	/**
	 * 得到监听器
	 * 
	 * @param listener
	 *            监听器
	 */
	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
	}

	/*
	 * (non-Javadoc)传感器检测到变化
	 * 
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	public void onSensorChanged(SensorEvent event) {
		long currentUpdateTime = System.currentTimeMillis();
		long timeInterval = currentUpdateTime - lastUpdateTime;
		if (timeInterval < UPTATE_INTERVAL_TIME) {
			return;
		}
		lastUpdateTime = currentUpdateTime;

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;

		lastX = x;
		lastY = y;
		lastZ = z;

		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
		if (speed >= SPEED_SHRESHOLD) {
			onShakeListener.onShake();// call listener shake method
		}
	}

	/*
	 * (non-Javadoc)精度变化时触发的回调方法
	 * 
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	/**
	 * The listener interface for receiving onShake events. The class that is interested in processing a onShake event
	 * implements this interface, and the object created with that class is registered with a component using the
	 * component's <code>addOnShakeListener<code> method. When
	 * the onShake event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnShakeEvent
	 */
	public interface OnShakeListener {

		/**
		 * 摇晃时的回调方法
		 */
		public void onShake();
	}

}