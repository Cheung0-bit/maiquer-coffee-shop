package tech.maiquer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.maiquer.system.domain.Evaluation;
import tech.maiquer.system.service.EvaluationService;
//import tech.maiquer.system.service.EvaluationService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

    @Resource
    private EvaluationService evaluationService;

    @GetMapping("/edit")
    public String editEva(Model model, Evaluation evaluation) {
        model.addAttribute("evaluation", evaluationService.queryById(evaluation.getId()).getData());
        return "evaluation/edit" ;
    }

}
