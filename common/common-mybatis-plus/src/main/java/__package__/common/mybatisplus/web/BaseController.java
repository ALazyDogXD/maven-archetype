package __package__.common.mybatisplus.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ALazyDogXD
 * @date 2022/6/12 0:39
 * @description 基础 Controller
 */

public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private final static String PAGE = "page";

    private final static String SIZE = "size";

    private final static String ORDER_BY_COLUMN = "orderByColumn";

    private final static String IS_DESC = "isDesc";

    /**
     * 分页查询
     *
     * @return 分页对象
     */
    protected <T> IPage<T> getPage() {
        HttpServletRequest request = getRequest();
        Integer page = toInt(request.getParameter(PAGE));
        Integer size = toInt(request.getParameter(SIZE));
        String orderByColumn = request.getParameter(ORDER_BY_COLUMN);
        Boolean isDesc = toBool(request.getParameter(IS_DESC));
        return getPage(page, size, orderByColumn, isDesc);
    }

    /**
     * 分页查询
     *
     * @param orderByColumn 排序字段
     * @param isDesc        是否倒序
     * @return 分页对象
     */
    protected <T> IPage<T> getPage(String orderByColumn, Boolean isDesc) {
        HttpServletRequest request = getRequest();
        Integer page = toInt(request.getParameter(PAGE));
        Integer size = toInt(request.getParameter(SIZE));
        return getPage(page, size, orderByColumn, isDesc);
    }

    /**
     * 分页查询
     *
     * @param page          当前页
     * @param size          每页条数
     * @param orderByColumn 排序字段
     * @param isDesc        是否倒序
     * @return 分页对象
     */
    protected <T> IPage<T> getPage(Integer page, Integer size, String orderByColumn, Boolean isDesc) {
        LOGGER.debug("当前页数: [{}], 每页数量: [{}], 排序字段: [{}], 是否降序: [{}]",
                page,
                size,
                orderByColumn,
                isDesc);
        if (Objects.isNull(page) || Objects.isNull(size)) {
            return new Page<>();
        }
        if (!StringUtils.isEmpty(orderByColumn)) {
            if (Optional.ofNullable(isDesc).orElse(false)) {
                return new Page<T>(page, size).addOrder(OrderItem.desc(orderByColumn));
            }
            return new Page<T>(page, size).addOrder(OrderItem.asc(orderByColumn));
        }
        return new Page<>(page, size);
    }

    private ServletRequestAttributes getAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    private HttpServletRequest getRequest() {
        return getAttributes().getRequest();
    }

    private Integer toInt(String v) {
        if (StringUtils.isEmpty(v)) {
            return null;
        } else {
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                throw new TypeMismatchException(v, Integer.class, e);
            }
        }
    }

    private Boolean toBool(String v) {
        if (StringUtils.isEmpty(v)) {
            return null;
        } else {
            return Boolean.parseBoolean(v);
        }
    }

}
