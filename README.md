# Bean-ValidateUtil
一个简洁的基于注解的Bean字段验证工具

用法说明
 @DataValidate(notNull = true,lengthLimit = "{3,5}",type = Long.class)
  private String type1;

  自定义一个验证规则
实现ValidateInterface接口，参照DigitsImpl.java

VResult b= BeanValidateUtil.vali2(finalInfo);
