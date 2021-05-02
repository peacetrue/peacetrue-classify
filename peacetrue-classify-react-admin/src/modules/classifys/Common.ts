import {Record} from "react-admin";

export const ParentIdReferenceProps = {
  source: 'parentId',
  reference: "classifys",
  perPage: 200,
  sort: {field: 'code', order: 'ASC'}
};

export const ReferencePropsBuilder = (source: string) => ({
  source: `${source}Id`,
  reference: "classifys",
  filter: {typeCode: source},
  perPage: 200,
  sort: {field: 'id', order: 'ASC'}
});

export const TextFormatter = (record: Record) => `${record.code}(${record.name})`;

