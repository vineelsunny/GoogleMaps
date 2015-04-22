package com.example.allplacesnearu;

import java.util.Comparator;

import com.google.android.gms.maps.model.LatLng;

public class BeanSort implements Comparator<BeanSort>{
	String name;
	LatLng lng;
	double distance;
	
	public BeanSort()
	{
		
	}
	
	public BeanSort(String nm,LatLng ln,double dist)
	{
		name=nm;
		lng=ln;
		distance=dist;
	}
	
	public String getName()
	{
		return name;
	}
	public LatLng getLatlng()
	{
		return lng;
	}
	public double getDistance()
	{
		return distance;
	}
	public void setName(String na)
	{
		name=na;
	}
	public void setLatlng(LatLng ln)
	{
		lng=ln;
	}
	public void setDistance(double f)
	{
		distance=f;
	}
	

	@Override
	public int compare(BeanSort lhs, BeanSort rhs) {
		
	if(lhs.getDistance()>rhs.getDistance())
	{
		return 1;
	}
	else if(lhs.getDistance()<rhs.getDistance())
	{
		return -1;
	}
		return 0;
	}

}
