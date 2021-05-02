import {PeaceEdit} from 'peacetrue-react-admin';
import * as React from 'react';
import {
  BooleanField,
  EditProps,
  maxLength,
  minValue,
  NumberInput,
  ReferenceField,
  required,
  SimpleForm,
  TextField,
  TextInput
} from 'react-admin';
import {UserCreateModifyFields} from "peacetrue-user";

export const ClassifyEdit = (props: EditProps) => {
  console.info('ClassifyEdit:', props);
  return (
    <PeaceEdit  {...props}>
      <SimpleForm>
        <ReferenceField source="parentId" reference="classifys" link={'show'}>
          <TextField source="name"/>
        </ReferenceField>
        <TextField source="code"/>
        <TextInput source="name" validate={[required(), maxLength(32)]}/>
        <TextField label={'层级'} source="level"/>
        <BooleanField source="leaf"/>
        <NumberInput source="serialNumber" validate={[required(), minValue(1)]} min={1}/>
        <TextInput source="remark" validate={[maxLength(255)]} fullWidth multiline/>
        {UserCreateModifyFields}
      </SimpleForm>
    </PeaceEdit>
  );
};
