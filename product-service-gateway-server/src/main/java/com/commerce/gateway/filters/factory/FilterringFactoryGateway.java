package com.commerce.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class FilterringFactoryGateway extends AbstractGatewayFilterFactory<FilterringFactoryGateway.Config> {

	private final Logger logger = LoggerFactory.getLogger(FilterringFactoryGateway.class);

	private final String LOG_INIT = "Ejecutando pre gateway filter factory";
	private final String LOG_MEDIA = "Ejecutando post gateway filter factory";
	
	public FilterringFactoryGateway() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			logger.info(LOG_INIT + config.getMsg());

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				Optional.ofNullable(config.getCookieValue()).ifPresent(cookie -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.getCookieName(), cookie).build());
				});

				logger.info(LOG_MEDIA + config.getMsg());
			}));
		};
	}
	
	@Override
	public String name() {
		return "CookieExampleGateway";
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("msg", "cookieName", "cookieValue");
	}

	public static class Config {
		
		private String msg;
		private String cookieName;
		private String cookieValue;

		public Config() { super(); }
		

		public Config(String msg, String cookieName, String cookieValue) {
			super();
			this.msg = msg;
			this.cookieName = cookieName;
			this.cookieValue = cookieValue;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getCookieValue() {
			return cookieValue;
		}

		public void setCookieValue(String cookieValue) {
			this.cookieValue = cookieValue;
		}

		public String getCookieName() {
			return cookieName;
		}

		public void setCookieName(String cookieName) {
			this.cookieName = cookieName;
		}

	}

}
