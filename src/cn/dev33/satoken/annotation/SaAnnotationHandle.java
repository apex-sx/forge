package cn.dev33.satoken.annotation;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.httpauth.basic.SaHttpBasicUtil;
import cn.dev33.satoken.httpauth.digest.SaHttpDigestUtil;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.strategy.SaAnnotationStrategy;
import cn.dev33.satoken.util.SaFoxUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Terraqo
 */
public enum SaAnnotationHandle implements SaAnnotationHandlerInterface {
    SaCheckDisableHandler(SaCheckDisable.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckDisable at = (SaCheckDisable) annotation;
            _checkMethod(at.type(), at.value(), at.level());
        }

        public static void _checkMethod(String type, String[] value, int level) {
            StpLogic stpLogic = SaManager.getStpLogic(type, false);

            Object loginId = stpLogic.getLoginId();
            for (String service : value) {
                stpLogic.checkDisableLevel(loginId, service, level);
            }
        }
    },
    SaCheckHttpBasicHandler(SaCheckHttpBasic.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckHttpBasic at = (SaCheckHttpBasic) annotation;
            _checkMethod(at.realm(), at.account());
        }

        public static void _checkMethod(String realm, String account) {
            SaHttpBasicUtil.check(realm, account);
        }
    },
    SaCheckHttpDigestHandler(SaCheckHttpDigest.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckHttpDigest at = (SaCheckHttpDigest) annotation;
            _checkMethod(at.username(), at.password(), at.realm(), at.value());
        }

        public static void _checkMethod(String username, String password, String realm, String value) {
            // 如果配置了 value，则以 value 优先
            if (SaFoxUtil.isNotEmpty(value)) {
                String[] arr = value.split(":");
                if (arr.length != 2) {
                    throw new SaTokenException("注解参数配置错误，格式应如：username:password");
                }
                SaHttpDigestUtil.check(arr[0], arr[1]);
                return;
            }

            // 如果配置了 username，则分别获取参数
            if (SaFoxUtil.isNotEmpty(username)) {
                SaHttpDigestUtil.check(username, password, realm);
                return;
            }

            // 都没有配置，则根据全局配置参数进行校验
            SaHttpDigestUtil.check();
        }

    },
    SaCheckLoginHandler(SaCheckLogin.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckLogin at = (SaCheckLogin) annotation;
            _checkMethod(at.type());
        }

        public static void _checkMethod(String type) {
            StpLogic stpLogic = SaManager.getStpLogic(type, false);

            stpLogic.checkLogin();
        }
    },
    SaCheckOrHandler(SaChecks.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaChecks at = (SaChecks) annotation;
            _checkMethod(at.login(), at.role(), at.permission(), at.safe(), at.httpBasic(), at.httpDigest(), at.disable(), at.append(), element);
        }

        public static void _checkMethod(
                SaCheckLogin[] login,
                SaCheckRole[] role,
                SaCheckPermission[] permission,
                SaCheckSafe[] safe,
                SaCheckHttpBasic[] httpBasic,
                SaCheckHttpDigest[] httpDigest,
                SaCheckDisable[] disable,
                Class<? extends Annotation>[] append,
                AnnotatedElement element
        ) {
            // 先把所有注解塞到一个 list 里
            List<Annotation> annotationList = new ArrayList<>();
            annotationList.addAll(Arrays.asList(login));
            annotationList.addAll(Arrays.asList(role));
            annotationList.addAll(Arrays.asList(permission));
            annotationList.addAll(Arrays.asList(safe));
            annotationList.addAll(Arrays.asList(disable));
            annotationList.addAll(Arrays.asList(httpBasic));
            annotationList.addAll(Arrays.asList(httpDigest));
            for (Class<? extends Annotation> annotationClass : append) {
                Annotation annotation = SaAnnotationStrategy.instance.getAnnotation.apply(element, annotationClass);
                if (annotation != null) {
                    annotationList.add(annotation);
                }
            }

            // 如果 atList 为空，说明 SaChecks 上不包含任何注解校验，我们直接跳过即可
            if (annotationList.isEmpty()) {
                return;
            }

            // 逐个开始校验 >>>
            List<SaTokenException> errorList = new ArrayList<>();
            for (Annotation item : annotationList) {
                try {
                    SaAnnotationStrategy.instance.annotationHandlerMap.get(item.annotationType()).check(item, element);
                    // 只要有一个校验通过，就可以直接返回了
                    return;
                } catch (SaTokenException e) {
                    errorList.add(e);
                }
            }

            // 执行至此，说明所有注解校验都通过不了，此时 errorList 里面会有多个异常，我们随便抛出一个即可
            throw errorList.get(0);
        }
    },
    SaCheckPermissionHandler(SaCheckPermission.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckPermission at = (SaCheckPermission) annotation;
            _checkMethod(at.type(), at.value(), at.mode(), at.orRole());
        }

        public static void _checkMethod(String type, String[] value, SaMode mode, String[] orRole) {
            StpLogic stpLogic = SaManager.getStpLogic(type, false);

            try {
                if (mode == SaMode.AND) {
                    stpLogic.checkPermissionAnd(value);
                } else {
                    stpLogic.checkPermissionOr(value);
                }
            } catch (NotPermissionException e) {
                // 权限认证校验未通过，再开始角色认证校验
                for (String role : orRole) {
                    String[] rArr = SaFoxUtil.convertStringToArray(role);
                    // 某一项 role 认证通过，则可以提前退出了，代表通过
                    if (stpLogic.hasRoleAnd(rArr)) {
                        return;
                    }
                }
                throw e;
            }
        }
    },
    SaCheckRoleHandler(SaCheckRole.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckRole at = (SaCheckRole) annotation;
            _checkMethod(at.type(), at.value(), at.mode());
        }

        public static void _checkMethod(String type, String[] value, SaMode mode) {
            StpLogic stpLogic = SaManager.getStpLogic(type, false);

            if (mode == SaMode.AND) {
                stpLogic.checkRoleAnd(value);
            } else {
                stpLogic.checkRoleOr(value);
            }
        }

    },
    SaCheckSafeHandler(SaCheckSafe.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaCheckSafe at = (SaCheckSafe) annotation;
            _checkMethod(at.type(), at.value());
        }

        public static void _checkMethod(String type, String value) {
            StpLogic stpLogic = SaManager.getStpLogic(type, false);

            stpLogic.checkSafe(value);
        }
    },
    SaIgnoreHandler(SaIgnore.class) {
        @Override
        public void check(Annotation annotation, AnnotatedElement element) {
            SaIgnore at = (SaIgnore) annotation;
            _checkMethod();
        }


        public static void _checkMethod() {
            SaRouter.stop();
        }
    },
    ;

    private final Class<? extends Annotation> handlerAnnotationClass;

    SaAnnotationHandle(Class<? extends Annotation> handlerAnnotationClass) {
        this.handlerAnnotationClass = handlerAnnotationClass;
    }

    @Override
    public final Class<? extends Annotation> getHandlerAnnotationClass() {
        return handlerAnnotationClass;
    }

    @Override
    public void check(Annotation annotation, AnnotatedElement annotatedElement) {

    }
}
