package com.koolearn.cloud.composition.dto;

import com.koolearn.cloud.composition.entity.Rule;
import com.koolearn.cloud.composition.entity.RuleItem;
import com.koolearn.cloud.composition.entity.RuleLev;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haozipu on 2016/7/25.
 */
public class RuleDto extends Rule implements Serializable {

    private List<RuleLev> ruleLevs;

    private List<RuleItem> ruleItems;

    public List<RuleLev> getRuleLevs() {
        return ruleLevs;
    }

    public void setRuleLevs(List<RuleLev> ruleLevs) {
        this.ruleLevs = ruleLevs;
    }

    public List<RuleItem> getRuleItems() {
        return ruleItems;
    }

    public void setRuleItems(List<RuleItem> ruleItems) {
        this.ruleItems = ruleItems;
    }
}
