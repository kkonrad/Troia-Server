package com.datascience.utils;

import java.util.Map;

/**
 * @Author: konrad
 */
public class MathHelpers {

	static public double getAverage(Map<?, Double> costs){
		//sum of object qualities is: n - sum_of_costs/s, where s in minSpammerCost
		double cnt = costs.size();
		double costSum = 0.;
		for (Double val : costs.values()){
			costSum += val;
		}
		return costSum/cnt;
	}
}
