package com.syc.china.feign;

import com.syc.china.entity.JWT;
import com.syc.china.feign.impl.AuthClientFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 远程调用8768授权服务器,获取AccessToken
 */
@FeignClient(value = "auth-server", fallback = AuthClientFeignHystrix.class)
public interface AuthClientFeign {


    /**
     * 注意:尽量不要用这种简写形式!
     * GetMapping这种简写形式,有可能会导致非法状态异常.
     * 成功获取AccessToken的示例:
     * {
     *     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzkwNjAxNjcsInVzZXJfbmFtZSI6InN5YyIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwianRpIjoiN2JjMDM4ODAtMTcxZi00NzZiLWJkOTMtYTc5M2YxOTk4MTVkIiwiY2xpZW50X2lkIjoiYXV0aC1jbGllbnQiLCJzY29wZSI6WyJzZXJ2aWNlIl19.ptdz9wtnGgMfimp-OoUfDYDWD3x0-oqZiR0ZXqWUmNk7YDMl09CGI5F8ufXDkhIpkw83mSW6fx39YqKfcmlBgsJDPhDvxN4_BzJ4OXZRpyXUKIfL0PAots_yxDrYHk_w8XJsJvzhsa9mZ62u37IMWFa5TdgFAuf47iajtDJzfU4wdIiWZg2JOzeQd_kY8LYCdKSrfXbrZIIZblO97SimdwzbUB4nt8MTYNP3o5Ko5YsWN-eME2c9qUkMf0epL0VGyFMFAVtueg6yenHS3dhNyFA6hgIxROleav6bATaQZCgnbluwmZo85b-Fjfoup05l9DbcGU3xEjsLyBwABpBklQ",
     *     "token_type": "bearer",
     *     "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJzeWMiLCJzY29wZSI6WyJzZXJ2aWNlIl0sImF0aSI6IjdiYzAzODgwLTE3MWYtNDc2Yi1iZDkzLWE3OTNmMTk5ODE1ZCIsImV4cCI6MTU4MTU2NTc2NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdLCJqdGkiOiJkMTNmMTQwMy1iMTgxLTQ3MGEtYjk1Ni1iMmVmYTk1MDVjYTgiLCJjbGllbnRfaWQiOiJhdXRoLWNsaWVudCJ9.NLDx7Dq9RmIMZWwkgjZDHouquGpZ03Wkftd7ZGx0uoWFVIxSuI02vl2BP_Ui2n8rvy2vL2TZfHf8wfB0zRoTm9cww35MfJLTFGQbMPYAilekUhtFnn9I0qMTp00WM35iRZc236WVA8g23M4xX0SiZBzaDwrR4e9RIXXhOADRzNOtGaCIW59tvyRespzc-KRXcSA_RDAcCbGTaA090jkPPQ8tpvzBlJmqa8sjZDjK1pTJBDlNmieI_Rrh59wyCROT36U5g7rj7zSTUMyBwc-WnCVtwI919aRcdGucNSMVKCbDXG7GJT990JhNEW1-kpn98aTPVdkc0-tLJn3aJVvIbA",
     *     "expires_in": 86399,
     *     "scope": "service",
     *     "jti": "7bc03880-171f-476b-bd93-a793f199815d"
     * }
     */
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    JWT getAccessToken(@RequestHeader("authorization") String authorization,
                       @RequestParam("grant_type") String grantType,
                       @RequestParam("username") String username,
                       @RequestParam("password") String password);

}
