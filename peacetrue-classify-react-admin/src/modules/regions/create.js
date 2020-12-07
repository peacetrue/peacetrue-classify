import React from 'react';
import {Create, required, SimpleForm, TextInput,} from 'react-admin';

export const RegionCreate = (props) => {
    console.info('RegionCreate:', props);
    return (
        <Create {...props} title={`新建${props.options.label}`}>
            <SimpleForm>
                <TextInput label={'编码'} source="code" validate={[required(),]}/>
                <TextInput label={'名称'} source="name" validate={[required(),]}/>
                <TextInput label={'备注'} source="remark" validate={[required(),]}/>
                <TextInput label={'父节点'} source="parentId" validate={[required(),]}/>
                <TextInput label={'层级'} source="level" validate={[required(),]}/>
            </SimpleForm>
        </Create>
    );
};
