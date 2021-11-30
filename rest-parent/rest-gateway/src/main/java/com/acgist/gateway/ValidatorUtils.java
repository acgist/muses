package com.acgist.gateway;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * 数据校验
 * 
 * @author acgist
 */
public final class ValidatorUtils {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
	private ValidatorUtils() {
	}
	
	/**
	 * 校验数据
	 * 
	 * @param 请求
	 * 
	 * @return 结果（非空字符表示失败）
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
