package com.acgist.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.acgist.gateway.Gateway;

/**
 * <p>工具 - 数据校验</p>
 * 
 * @author acgist
 */
public final class ValidatorUtils {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	private ValidatorUtils() {
	}
	
	/**
	 * <p>校验数据</p>
	 * <p>成功返回：null或者空字符串</p>
	 * <p>失败返回：错误信息</p>
	 * 
	 * @param 请求
	 * 
	 * @return 结果
	 */
	public static final String verify(Gateway request) {
		if(request == null) {
			return null;
		}
		final StringBuilder message = new StringBuilder();
		final Set<ConstraintViolation<Gateway>> set = VALIDATOR.validate(request, Default.class);
		if (set != null && !set.isEmpty()) {
			for (ConstraintViolation<Gateway> violation : set) {
				message
					.append(violation.getMessage())
					.append("[")
					.append(violation.getPropertyPath().toString())
					.append("]")
					.append("&");
			}
		}
		if(message.length() != 0) {
			message.setLength(message.length() - 1);
		}
		return message.toString();
	}
	
}
