import React from 'react';
import {Edit, required, SimpleForm, TextInput} from 'react-admin';

export const ClassifyEdit = (props) => {
    console.info('ClassifyEdit:', props);
    return (
        <Edit {...props} title={`${props.options.label}#${props.id}`}>
            <SimpleForm>
                <TextInput label={'编码'} source="code" validate={required()}/>
                <TextInput label={'名称'} source="name" validate={required()}/>
                <TextInput label={'备注'} source="remark" validate={required()}/>
                <TextInput label={'父节点'} source="parentId" validate={required()}/>
                <TextInput label={'层级'} source="level" validate={required()}/>
            </SimpleForm>
        </Edit>
    );
};
