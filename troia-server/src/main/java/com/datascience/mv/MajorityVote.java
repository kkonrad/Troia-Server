package com.datascience.mv;

import com.datascience.core.base.*;
import com.datascience.core.stats.IErrorRateCalculator;
import com.datascience.gal.DatumResult;
import com.datascience.utils.ProbabilityDistributions;

import java.util.Collection;
import java.util.Map;

/**
 * @Author: konrad
 */
public abstract class MajorityVote extends NominalAlgorithm {

	public MajorityVote(IErrorRateCalculator errorRateCalculator){
		super(errorRateCalculator);
	}

	public void computeResultsForObject(LObject<String> object){
		DatumResult dr = results.getOrCreateDatumResult(object);
		dr.setCategoryProbabilites(objectLabelDistribution(object));
	}

	public Map<String, Double> objectLabelDistribution(LObject<String> object){
		Collection<String> categories = data.getCategoriesNames();
		if (object.isGold()) {
			return ProbabilityDistributions.generateGoldDistribution(categories, object.getGoldLabel());
		}
		Collection<AssignedLabel<String>> assigns = data.getAssignsForObject(object);
		if (assigns.isEmpty()) {
			return generateLabelForNonAssignedObject();
		}
		return generateLabelDistribution(categories, assigns);
	}

	public Map<String, Double> generateLabelForNonAssignedObject(){
		return ProbabilityDistributions.getPriorBasedDistribution(data);
	}

	public Map<String, Double> generateLabelDistribution(Collection<String> categories,
				Collection<AssignedLabel<String>> assigns){
		return ProbabilityDistributions.generateMV_PD(categories, assigns);
	}
}