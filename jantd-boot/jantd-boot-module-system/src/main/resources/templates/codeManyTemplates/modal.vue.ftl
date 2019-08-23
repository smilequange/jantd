<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
<#list table.fields as field><#rt/>
  <#if field_index % 2 == 0 ><#rt/>
        <a-row>
    <#if field.propertyName !='id' && !"${cfg.pageFilterFields}"?contains(field.propertyName)>
      <#list [field_index, field_index+1] as idx><#rt/>
        <#if idx lt table.fields?size>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="${table.fields[idx].comment }">
            <#if table.fields[idx].type =='date'>
              <a-date-picker
                placeholder="请输入${table.fields[idx].comment}"
                style="width:100%"
                v-decorator="[ '${table.fields[idx].propertyName}', <#if table.fields[idx].customMap.NULL =='NO'>validatorRules.${table.fields[idx].propertyName} <#else>{}</#if>]"/>
            <#elseif table.fields[idx].type =='datetime'>
              <a-date-picker
                placeholder="请输入${table.fields[idx].comment}"
                style="width:100%"
                :showTime="true"
                format="YYYY-MM-DD HH:mm:ss"
                v-decorator="[ '${table.fields[idx].propertyName}', <#if table.fields[idx].customMap.NULL =='NO'>validatorRules.${table.fields[idx].propertyName} <#else>{}</#if>]"/>
            <#elseif "int,decimal,double,"?contains(table.fields[idx].type)>
              <a-input-number placeholder="请输入${table.fields[idx].comment}" style="width:100%" v-decorator="[ '${table.fields[idx].propertyName}', <#if table.fields[idx].customMap.NULL =='NO'>validatorRules.${table.fields[idx].propertyName} <#else>{}</#if>]"/>
            <#else>
              <a-input placeholder="请输入${table.fields[idx].comment}" v-decorator="['${table.fields[idx].propertyName}', <#if table.fields[idx].customMap.NULL =='NO'>validatorRules.${table.fields[idx].propertyName} <#else>{}</#if>]"/>
            </#if>
            </a-form-item>
          </a-col>
        </#if>
      </#list><#rt/>
    </#if><#rt/>
        </a-row>
  </#if><#rt/>
</#list>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
<#list cfg.subTables as sub><#rt/>
        <a-tab-pane tab="${sub.ftlDescription}" :key="refKeys[${sub_index}]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[${sub_index}]"
            :loading="${sub.entity?uncap_first}Table.loading"
            :columns="${sub.entity?uncap_first}Table.columns"
            :dataSource="${sub.entity?uncap_first}Table.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
</#list>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import moment from 'moment'
  import pick from 'lodash.pick'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'

  export default {
    name: '${entity}Modal',
    mixins: [JEditableTableMixin],
    data() {
      return {
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        <#list table.fields as field>
        <#if field.propertyName !='id'>
        <#if field.customMap.NULL =='NO'>
        ${field.propertyName}:{rules: [{ required: true, message: '请输入${field.comment}!' }]},
        </#if>
        </#if>
        </#list>
        },
        refKeys: [<#list cfg.subTables as sub>'${sub.entity?uncap_first}', </#list>],
        activeKey: '${cfg.subTables[0].entity?uncap_first}',
<#list cfg.subTables as sub><#rt/>
        // ${sub.ftlDescription}
        ${sub.entity?uncap_first}Table: {
          loading: false,
          dataSource: [],
          columns: [
<#list sub.colums as col><#rt/>
    <#if col.filedComment !='外键'>
            {
              title: '${col.filedComment}',
              key: '${col.fieldName}',
      <#if col.fieldType =='date'>
              type: FormTypes.date,
      <#elseif col.fieldType =='datetime'>
              type: FormTypes.datetime,
      <#elseif "int,decimal,double,"?contains(col.fieldType)>
              type: FormTypes.inputNumber,
      <#else>
              type: FormTypes.input,
      </#if>
              defaultValue: '',
              placeholder: '请输入${'$'}{title}',
      <#if col.nullable =='NO'>
              validateRules: [{ required: true, message: '${'$'}{title}不能为空' }],
      </#if>
            },
    </#if>
</#list>
          ]
        },
</#list>
        url: {
          add: "/${entity?uncap_first}/${entity?uncap_first}/add",
          edit: "/${entity?uncap_first}/${entity?uncap_first}/edit",
<#list cfg.subTables as sub><#rt/>
          ${sub.entity?uncap_first}: {
            list: '/${entity?uncap_first}/${entity?uncap_first}/query${sub.entity}ByMainId'
          },
</#list>
        }
      }
    },
    methods: {
 
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model<#list table.fields as field><#if field.propertyName !='id' && field.type?index_of("date")==-1 &&!"createBy,updateBy"?contains(field.propertyName)>,'${field.propertyName}'</#if></#list>))
          //时间格式化
          <#list table.fields as field>
          <#if field.propertyName !='id' && field.type?index_of("date")!=-1 && !"createTime,updateTime"?contains(field.propertyName)>
          this.form.setFieldsValue({${field.propertyName}:this.model.${field.propertyName}?moment(this.model.${field.propertyName}):null})
        </#if>
        </#list>
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
<#list cfg.subTables as sub><#rt/>
          this.requestSubTableData(this.url.${sub.entity?uncap_first}.list, params, this.${sub.entity?uncap_first}Table)
</#list>
        }
      },
 
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        //时间格式化`
<#list table.fields as field><#rt/>
	<#if field.propertyName !='id' && field.type =='date'>
        main.${field.propertyName} = main.${field.propertyName} ? main.${field.propertyName}.format() : null;
	<#elseif field.propertyName !='id' && field.type =='datetime' && !"createTime,updateTime"?contains(field.propertyName)>
        main.${field.propertyName} = main.${field.propertyName} ? main.${field.propertyName}.format('YYYY-MM-DD HH:mm:ss') : null;
	</#if>
</#list>
        return {
          ...main, // 展开
<#list cfg.subTables as sub><#rt/>
          ${sub.entity?uncap_first}List: allValues.tablesValue[${sub_index}].values,
</#list>
        }
      }
    }
  }
</script>

<style scoped>
</style>