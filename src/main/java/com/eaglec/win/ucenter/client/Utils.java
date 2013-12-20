package com.eaglec.win.ucenter.client;

import java.util.Map;
import java.util.ResourceBundle;

public class Utils extends PHPFunctions{
	
	/**
	 * 字符串加密以及解密函数
	 *
	 * @param string $string	原文或者密文
	 * @param string $operation	操作(ENCODE | DECODE), 默认为 DECODE
	 * @param string $key		密钥
	 * @param int $expiry		密文有效期, 加密时候有效， 单位 秒，0 为永久有效
	 * @return string		处理后的 原文或者 经过 base64_encode 处理后的密文
	 *
	 * @example
	 *
	 * 	$a = authcode('abc', 'ENCODE', 'key');
	 * 	$b = authcode($a, 'DECODE', 'key');  // $b(abc)
	 *
	 * 	$a = authcode('abc', 'ENCODE', 'key', 3600);
	 * 	$b = authcode('abc', 'DECODE', 'key'); // 在一个小时内，$b(abc)，否则 $b 为空
	 */
	public static String uc_authcode(String $string, String $operation){
		return uc_authcode($string, $operation, null);
	}
	public static String uc_authcode(String $string, String $operation, String $key){
		return uc_authcode($string, $operation, $key, 0);
	}
	public static String uc_authcode(String $string, String $operation, String $key,int $expiry ) {

		int $ckey_length = 4;	
		//note 随机密钥长度 取值 0-32;
		//note 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。
		//note 取值越大，密文变动规律越大，密文变化 = 16 的 $ckey_length 次方
		//note 当此值为 0 时，则不产生随机密钥

//		$key = md5( $key!=null ? $key : ResourceBundle.getBundle("config").getString("oauth.client_secret"));
		String $keya = md5(substr($key, 0, 16));
		String $keyb = md5(substr($key, 16, 16));
		String $keyc = $ckey_length > 0? ($operation.equals("DECODE") ? substr($string, 0, $ckey_length): substr(md5(microtime()), -$ckey_length)) : "";

		String $cryptkey = $keya + md5( $keya + $keyc);
		int $key_length = $cryptkey.length();

		$string = $operation.equals("DECODE") ? base64_decode(substr($string, $ckey_length)) : sprintf("%010d", $expiry>0 ? $expiry + time() : 0)+substr(md5($string+$keyb), 0, 16)+$string;
		int $string_length = $string.length();

		StringBuffer $result1 = new StringBuffer();

		int[] $box = new int[256];
		for(int i=0;i<256;i++){
			$box[i] = i;
		}

		int[] $rndkey = new int[256];
		for(int $i = 0; $i <= 255; $i++) {
			$rndkey[$i] = (int)$cryptkey.charAt($i % $key_length);
		}

		int $j=0;
		for(int $i = 0; $i < 256; $i++) {
			$j = ($j + $box[$i] + $rndkey[$i]) % 256;
			int $tmp = $box[$i];
			$box[$i] = $box[$j];
			$box[$j] = $tmp;
		}

		$j=0;
		int $a=0;
		for(int $i = 0; $i < $string_length; $i++) {
			$a = ($a + 1) % 256;
			$j = ($j + $box[$a]) % 256;
			int $tmp = $box[$a];
			$box[$a] = $box[$j];
			$box[$j] = $tmp;
			
			$result1.append((char)( ((int)$string.charAt($i)) ^ ($box[($box[$a] + $box[$j]) % 256])));
			
		}

		if($operation.equals("DECODE")) {
			String $result = $result1.substring(0, $result1.length());
			if((Integer.parseInt(substr($result.toString(), 0, 10)) == 0 || Long.parseLong(substr($result.toString(), 0, 10)) - time() > 0) && substr($result.toString(), 10, 16).equals( substr(md5(substr($result.toString(), 26)+ $keyb), 0, 16))) {
				return substr($result.toString(), 26);
			} else {
				return "";
			}
		} else {
			return $keyc+base64_encode($result1.toString()).replaceAll("=", "");
		}
	}

	public static void parse_str(String str, Map<String,String> sets){
		if(str==null||str.length()<1) 
			return;
		String[] ps = str.split("&");
		for(int i=0;i<ps.length;i++){
			String[] items = ps[i].split("=");
			if(items.length==2){
				sets.put(items[0], items[1]);
			}else if(items.length ==1){
				sets.put(items[0], "");
			}
		}
	}
	
	public static long time(){
		return System.currentTimeMillis()/1000;
	}
	
    public static long tolong(Object s){
        if(s!=null){
            String ss = s.toString().trim();
            if(ss.length()==0){
                return 0L;
            }else{
                return Long.parseLong(ss);
            }
        }else{
            return 0L;
        }
    }

	
	@Override
	public String uc_api_post(String $module, String $action,
			Map<String, Object> $arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uc_api_mysql(String $model, String $action, Map $args) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args){
		System.out.println(uc_authcode("wzlelesdaf2342342343242342123123123123123","123sdf12wdsa234234234234123123123123123123123","12s21311sfds234234324234234sdfsafsadfsadfsadfsadfsadfsdasdfsadfsadf",3600));
	}
}
