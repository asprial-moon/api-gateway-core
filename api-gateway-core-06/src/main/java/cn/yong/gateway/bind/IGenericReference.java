package cn.yong.gateway.bind;

import java.util.Map;

/**
 * @author Line
 * @desc 统一泛化调用接口
 * @date 2022/10/30
 */
public interface IGenericReference {

    Object $invoke(Map<String, Object> params);

}
