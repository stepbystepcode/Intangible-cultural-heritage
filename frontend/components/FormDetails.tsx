//FormDetails.tsx
import React, { useState, ChangeEvent, FormEvent } from 'react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

// 为字段配置定义类型
interface FormField {
  name: string;
  label: string;
  type: 'text' | 'date' | 'number'; // 这里可以根据实际需求进一步扩展类型
  placeholder: string;
}

// 为表单配置定义类型
interface FormFields {
  [key: string]: FormField[]; // key 是 Tab 名称，值是一个字段数组
}

interface FormDetailsProps {
  selectedTab: string;
  onSubmit: (data: Record<string, string | number>) => void;
}

// 表单字段配置
const formFields: FormFields = {
  '非物质文化遗产项目': [
    { name: 'projectName', label: '项目名称', type: 'text', placeholder: '输入项目名称' },
    { name: 'category', label: '分类', type: 'text', placeholder: '输入分类' },
    { name: 'announcementDate', label: '公布日期', type: 'date', placeholder: '' },
    { name: 'type', label: '类型', type: 'text', placeholder: '输入类型' },
    { name: 'applicationRegion', label: '区域', type: 'text', placeholder: '输入区域' },
  ],
  '传承人': [
    { name: 'name', label: '姓名', type: 'text', placeholder: '输入姓名' },
    { name: 'gender', label: '性别', type: 'text', placeholder: '输入性别' },
    { name: 'ethnicity', label: '民族', type: 'text', placeholder: '输入民族' },
    { name: 'category', label: '类别', type: 'text', placeholder: '输入类别' },
    { name: 'projectID', label: '关联项目ID', type: 'text', placeholder: '输入项目ID' },
  ],
  '保护单位': [
    { name: 'unitName', label: '单位名称', type: 'text', placeholder: '输入单位名称' },
    { name: 'region', label: '区域', type: 'text', placeholder: '输入区域' },
    { name: 'contactInfo', label: '联系方式', type: 'text', placeholder: '输入联系方式' },
  ],
};

const FormDetails: React.FC<FormDetailsProps> = ({ selectedTab, onSubmit }) => {
  const [formData, setFormData] = useState<Record<string, string | number>>({});
  const [isSelecting, setIsSelecting] = useState(false); // 管理是否进入选点模式

  // 输入变化处理
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // 表单提交处理
  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    onSubmit(formData); // 调用父组件传来的 onSubmit 方法
  };

  // 获取当前 Tab 的字段配置
  const currentFields = formFields[selectedTab] || [];

  // 切换选点模式
  const handleSelectMode = () => {
    setIsSelecting(true);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {currentFields.map((field) => (
        <div key={field.name} className="space-y-2">
          <label htmlFor={field.name} className="font-semibold">{field.label}</label>
          <Input
            type={field.type}
            name={field.name}
            id={field.name}
            value={formData[field.name] || ''}
            onChange={handleChange}
            placeholder={field.placeholder}
            className="input"
          />
        </div>
      ))}

      {/* 触发选点模式的按钮 */}
      <Button
        type="button"
        className="btn btn-primary"
        onClick={handleSelectMode}
      >
        {isSelecting ? '选点模式已开启' : '地图选点'}
      </Button>

      <Button type="submit" className="btn btn-primary">提交</Button>
    </form>
  );
};

export default FormDetails;