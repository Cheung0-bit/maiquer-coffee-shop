package tech.maiquer.system.service;

import org.springframework.stereotype.Service;
import tech.maiquer.system.domain.Evaluation;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.mapper.EvaluationMapper;
import tech.maiquer.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author lin
 * @Description
 * @Date: 2022/4/17 15:01
 */
@Service
public class SysMessage {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    public String constructMessage(Integer evaId, Long userId) {

        SysUser sysUser = sysUserMapper.queryById(userId);
        Evaluation evaluation = evaluationMapper.queryById(evaId);

        String nickName = sysUser.getNickname();
        String evaName = evaluation.getName();
        BigDecimal price = evaluation.getPrice();

        return String.format("微信用户: %s\n购买了测评: %s\n支付了: %.2f元", nickName, evaName, price);

    }


}
