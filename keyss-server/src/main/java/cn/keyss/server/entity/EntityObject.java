package cn.keyss.server.entity;


import cn.keyss.common.utilities.XmlStringHelper;

import javax.validation.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 实体对象
 */
public class EntityObject implements Cloneable, Serializable {

    @Override
    public String toString() {
        try {
            return XmlStringHelper.saveToXmlString(this.getClass(), this);
        } catch (Throwable t) {
            throw new EntityException("实体转为字符串异常！", t);
        }
    }

    protected void selfValidate(Set<ConstraintViolation<EntityObject>> results, Class<?>... groups) {

    }

    public Set<ConstraintViolation<EntityObject>> validate(Class<?>... groups) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EntityObject>> violations = validator.validate(this, groups);
        return violations;
    }

    public void validateAndThrowException(Class<?>... groups) {
        Set<ConstraintViolation<EntityObject>> results = validate(groups);

        if (!results.isEmpty()) {
            throw new ConstraintViolationException(String.format("实体对象‘%s’无效，验证失败！", this.getClass().getName()), results);
        }
    }
}