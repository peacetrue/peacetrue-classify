import * as React from 'react';
import {
  CloneButton,
  CreateButton,
  Datagrid,
  EditButton,
  Filter,
  List,
  ListProps,
  ReferenceField,
  sanitizeListRestProps,
  TextField,
  TextInput,
  TopToolbar,
  useListContext
} from 'react-admin';
import RearrangeButton from "./RearrangeButton";
import GenerateButton from "./GenerateButton";
import {ExporterBuilder, useWidthStyles} from "peacetrue-react-admin";
import ClassifyMessages from "./Messages";
import {UserCreatedTimeFilter, UserCreateFields} from "peacetrue-user";

const ListActions = (props: any) => {
  const {
    className,
    exporter,
    filters,
    maxResults,
    ...rest
  } = props;
  const {
    resource,
    basePath,
    displayedFilters,
    filterValues,
    showFilter,
  } = useListContext();
  return (
    <TopToolbar className={className} {...sanitizeListRestProps(rest)}>
      {filters && React.cloneElement(filters, {
        resource,
        showFilter,
        displayedFilters,
        filterValues,
        context: 'button',
      })}
      <CreateButton resource={resource} basePath={basePath}/>
      <RearrangeButton/>
    </TopToolbar>
  );
};


const Filters = (props: any) => (
  <Filter {...props}>
    {/*<ReferenceInput source="typeId" reference="dictionary-values"
                        filter={{dictionaryTypeCode: 'classifyType'}}
                        allowEmpty alwaysOn>
            <SelectInput source="name" resettable/>
        </ReferenceInput>*/}
    {/*
        <ReferenceInput {...parentIdReferenceProps} allowEmpty alwaysOn>
            <SelectInput source="name" optionText={textFormatter} resettable/>
        </ReferenceInput>
*/}
    <TextInput source="code" allowEmpty alwaysOn resettable/>
    <TextInput source="name" allowEmpty alwaysOn resettable/>
    {UserCreatedTimeFilter}
  </Filter>
);

export const ClassifyList = (props: ListProps) => {
  console.info('ClassifyList:', props);
  let classes = useWidthStyles();
  return (
    <List {...props}
          actions={<ListActions/>}
          filters={<Filters/>}
          filter={{parentId: 2}}
          exporter={ExporterBuilder(ClassifyMessages.resources.classifys)}
          sort={{field: 'id', order: 'ASC'}}
    >
      <Datagrid rowClick="show">
        <ReferenceField source="parentId" reference="classifys">
          <TextField source="name"/>
        </ReferenceField>
        <TextField source="code" cellClassName={classes.width10}/>
        <TextField source="name" cellClassName={classes.width10}/>
        {/*<TextField source="level"/>*/}
        {/*<BooleanField source="leaf"/>*/}
        <TextField source="serialNumber"/>
        {UserCreateFields}
        {/*<ChildrenButton/>*/}
        <EditButton/>
        <CloneButton/>
        <GenerateButton/>
      </Datagrid>
    </List>
  )
};
