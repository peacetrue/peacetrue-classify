import {PeaceShow} from 'peacetrue-react-admin';
import * as React from 'react';
import {BooleanField, ReferenceField, ShowProps, SimpleShowLayout, TextField} from 'react-admin';
import {UserCreateModifyFields} from "peacetrue-user";

export const ClassifyShow = (props: ShowProps) => {
  console.info('ClassifyShow:', props);
  return (
    <PeaceShow {...props}>
      <SimpleShowLayout>
        <ReferenceField source="parentId" reference="classifys">
          <TextField source="name"/>
        </ReferenceField>
        <TextField source="code"/>
        <TextField source="name"/>
        <TextField source="level"/>
        <BooleanField source="leaf"/>
        <TextField source="serialNumber"/>
        <TextField source="remark"/>
        {UserCreateModifyFields}
      </SimpleShowLayout>
    </PeaceShow>
  );
};
