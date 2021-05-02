import {Messages} from "peacetrue-user";

export const ClassifyMessages = {
  resources: {
    classifys: {
      name: '分类',
      fields: {
        code: '编码',
        name: '名称',
        remark: '备注',
        typeId: '类型',
        parentId: '父节点',
        level: '级别',
        leaf: '叶子节点',
        serialNumber: '序号',
        ...Messages
      }
    },
  }
}

export default ClassifyMessages;
