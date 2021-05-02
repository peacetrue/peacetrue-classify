import * as React from 'react';
import {
  Create,
  CreateProps,
  maxLength,
  ReferenceInput,
  required,
  SelectInput,
  SimpleForm,
  TextInput
} from 'react-admin';
import {ParentIdReferenceProps, TextFormatter} from "./Common";

export const ClassifyCreate = (props: CreateProps) => {
  console.info('ClassifyCreate:', props);
  return (
    <Create {...props}>
      <SimpleForm>
        <ReferenceInput {...ParentIdReferenceProps}>
          <SelectInput source="name" optionText={TextFormatter} validate={[required(),]}/>
        </ReferenceInput>
        <TextInput source="code" validate={[required(),]}/>
        <TextInput source="name" validate={[required(), maxLength(32)]}/>
        <TextInput source="remark" validate={[maxLength(255)]} multiline fullWidth/>
      </SimpleForm>
    </Create>
  );
};
