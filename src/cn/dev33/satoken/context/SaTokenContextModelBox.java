package cn.dev33.satoken.context;

/**
 * Box 盒子类，用于存储 [ SaRequest、SaResponse、SaStorage ] 三个包装对象
 *
 * @author click33
 * @since 1.16.0
 */
public record SaTokenContextModelBox(SaRequest request,SaResponse response,SaStorage storage) {

}