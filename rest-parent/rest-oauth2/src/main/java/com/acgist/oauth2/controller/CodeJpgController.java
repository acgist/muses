package com.acgist.oauth2.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.oauth2.filter.CodeAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/oauth2")
public class CodeJpgController {
	
	// 宽度
	private int width = 60;
	// 高度
	private int height = 34;
	// 字符熟练
	private int codeCount = 4;
	// 起始X坐标
	private int codeX;
	// 起始Y坐标
	private int codeY;
	// 字体大小
	private int fontHeight;
	
	/**
	 * 随机字符：去掉混淆字符（0、1、I、O）
	 */
	private static final char[] CODE_SEQUENCE = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'2', '3', '4', '5', '6', '7', '8', '9'
	};

	@PostConstruct
	public void init() throws ServletException {
		this.codeX = this.width / this.codeCount;
		this.codeY = this.height - 4;
		this.fontHeight = Math.max(this.codeX, this.codeY) - 2;
		log.info("图形验证码配置：{}-{}-{}", this.width, this.height, this.codeCount);
	}

	@GetMapping("/code.jpg")
	public void jpg(HttpServletRequest request, HttpServletResponse response) {
		final Random random = new Random();
		final BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2d = image.createGraphics();
		// 背景
		graphics2d.setColor(Color.WHITE);
		graphics2d.fillRect(0, 0, this.width, this.height);
		// 边框
		graphics2d.setColor(Color.PINK);
		graphics2d.drawRect(0, 0, this.width, this.height);
		// 干扰
		graphics2d.setColor(this.buildRandomColor(random));
		for (int index = 0; index < this.codeCount * Byte.SIZE; index++) {
			final int x = random.nextInt(this.width);
			final int y = random.nextInt(this.height);
			final int xl = random.nextInt(Short.SIZE);
			final int yl = random.nextInt(Short.SIZE);
			graphics2d.drawLine(x, y, x + xl, y + yl);
		}
		// 噪点
		final float yawpRate = 0.05F;
		final int area = (int) (yawpRate * this.width * this.height);
		for (int index = 0; index < area; index++) {
			final int x = random.nextInt(this.width);
			final int y = random.nextInt(this.height);
			final int rgb = this.buildRandomColorInt(random);
			image.setRGB(x, y, rgb);
		}
		// 字体
		final Font font = new Font("Fixedsys", Font.PLAIN, this.fontHeight);
//		final Font font = new Font("Algerian", Font.ITALIC, this.fontHeight);
		graphics2d.setFont(font);
		// Code
		final StringBuilder code = new StringBuilder();
		for (int index = 0; index < this.codeCount; index++) {
			final char value = CODE_SEQUENCE[random.nextInt(CODE_SEQUENCE.length)];
			code.append(value);
			// 扭曲
//			final AffineTransform affineTransform = new AffineTransform();
//			affineTransform.setToRotation(Math.PI * random.nextDouble() * (random.nextBoolean() ? 1 : -1), index * this.codeX, this.codeY);
//			graphics2d.setTransform(affineTransform);
			graphics2d.setColor(this.buildRandomColor(random));
			graphics2d.drawChars(new char[] {value}, 0, 1, index * this.codeX, this.codeY);
		}
		request.getSession().setAttribute(CodeAuthenticationFilter.CODE, code.toString());
		// 禁止缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpg");
		try {
			ImageIO.write(image, "jpg", response.getOutputStream());
		} catch (IOException e) {
			log.error("图形验证码输出失败", e);
		}
	}
	
	private Color buildRandomColor(Random random) {
		final int[] rgb = this.buildRandomRGB(random);
		return new Color(rgb[0], rgb[1], rgb[2]);
	}
	
	private int buildRandomColorInt(Random random) {
		int color = 0;
		final int[] rgb = this.buildRandomRGB(random);
		for (int value : rgb) {
			color = color << 8;
			color = color | value;
		}
		return color;
	}
	
	private int[] buildRandomRGB(Random random) {
		final int[] rgb = new int[3];
		for (int index = 0; index < 3; index++) {
			rgb[index] = 100 + random.nextInt(100);
		}
		return rgb;
	}
	
}
