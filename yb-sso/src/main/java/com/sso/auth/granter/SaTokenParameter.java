package com.sso.auth.granter;

import lombok.Data;
import org.springblade.core.tool.support.Kv;

/**
 * TokenParameter
 *
 * @author Jenny wang
 */
@Data
public class SaTokenParameter {

	private Kv args = Kv.init();

}
