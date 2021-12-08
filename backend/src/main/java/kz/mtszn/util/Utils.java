package kz.mtszn.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static String getRequestRemoteAddress(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();

            String forwardedFor = request.getHeader("X-FORWARDED-FOR");
            if(StringUtils.isNotEmpty(forwardedFor)){
                return forwardedFor;
            }

            final String remoteAddr = request.getRemoteAddr();
            if(StringUtils.isNotEmpty(remoteAddr)) {
                return remoteAddr;
            }

            return "unknown";
        } catch (Exception e){
            return "system";
        }
    }

    public static Boolean isNullOrEmpty(String value) {
        return (value == null || value.trim().isEmpty());
    }
}
