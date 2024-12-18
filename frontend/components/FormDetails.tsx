//FormDetails.tsx
import React, { useState, ChangeEvent, FormEvent } from 'react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { useGlobalStore } from "@/lib/store";
import { cn } from "@/lib/utils";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Check, ChevronsUpDown } from "lucide-react";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { ethnicities } from '@/lib/data';
import axios from 'axios';

// 为字段配置定义类型
interface FormField {
  name: string;
  label: string;
  type: 'text' | 'date' | 'number' | 'select';
  placeholder: string;
  options?: { value: string; label: string; }[];
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
    { 
      name: 'gender', 
      label: '性别', 
      type: 'select', 
      placeholder: '选择性别',
      options: [
        { value: '男', label: '男' },
        { value: '女', label: '女' }
      ]
    },
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

// 添加手机号验证函数
const isValidPhone = (phone: string) => {
  return /^1[3-9]\d{9}$/.test(phone);
};

// 添加 API 端点映射
const API_ENDPOINTS = {
  '传承人': '/api/inheritors',
  '非物质文化遗产项目': '/api/project',
  '保护单位': '/api/protection-units'
} as const;

const FormDetails: React.FC<FormDetailsProps> = ({ selectedTab, onSubmit }) => {
  const [formData, setFormData] = useState<Record<string, string | number>>({});
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [ethnicityOpen, setEthnicityOpen] = React.useState(false);

  const isSelecting = useGlobalStore((state) => state.isSelecting);
  const toggleSelecting = useGlobalStore((state) => state.toggleSelecting);
  const newPosition = useGlobalStore((state) => state.newPosition);
  // 输入变化处理
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // 表单提交处理
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    
    // 重置所有错误
    setErrors({});
    
    // 检查空字段并收集错误
    const newErrors: Record<string, string> = {};
    let firstEmptyField = null as HTMLInputElement | null;
    
    currentFields.forEach(field => {
      if (!formData[field.name]) {
        newErrors[field.name] = `请输入${field.label}`;
        const element = document.querySelector(`input[name="${field.name}"]`);
        if (element instanceof HTMLInputElement) {
          firstEmptyField = element;
        }
      } else if (field.name === 'contactInfo' && !isValidPhone(formData[field.name].toString())) {
        newErrors[field.name] = '请输入正确的11位手机号';
        const element = document.querySelector(`input[name="${field.name}"]`);
        if (element instanceof HTMLInputElement && !firstEmptyField) {
          firstEmptyField = element;
        }
      }
    });

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      // 聚焦第一个空字段
      firstEmptyField && firstEmptyField.focus();
      return;
    }

    if (newPosition === null) {
      setErrors({ map: '请在地图上选点' });
      return;
    }

    // 处理表单数据
    const processedData = {
      ...formData,
      projectId: Number(formData.projectID) || 0,
      latitude: newPosition.lat,
      longitude: newPosition.lng
    };

    try {
      const endpoint = API_ENDPOINTS[selectedTab as keyof typeof API_ENDPOINTS];
      const response = await axios.post(endpoint, processedData);
      onSubmit(response.data);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setErrors({ submit: error.response?.data?.message || '提交失败，请重试' });
      } else {
        setErrors({ submit: '提交失败，请重试' });
      }
      console.error('提交错误:', error);
    }
  };

  // 获取当前 Tab 的字段配置
  const currentFields = formFields[selectedTab] || [];

  // Add a new handler for select changes
  const handleSelectChange = (name: string, value: string) => {
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {currentFields.map((field) => (
        <div key={field.name} className="space-y-1">
          <label htmlFor={field.name} className="font-semibold">
            {field.label}
          </label>
          {field.name === 'ethnicity' ? (
            <Popover open={ethnicityOpen} onOpenChange={setEthnicityOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={ethnicityOpen}
                  className="w-full justify-between"
                >
                  {formData[field.name]
                    ? ethnicities.find((item) => item === formData[field.name])
                    : "选择民族..."}
                  <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-full p-0 z-[9999]">
                <Command>
                  <CommandInput placeholder="搜索民族..." />
                  <CommandList>
                    <CommandEmpty>未找到相关民族</CommandEmpty>
                    <CommandGroup>
                      {ethnicities.map((ethnicity) => (
                        <CommandItem
                          key={ethnicity}
                          value={ethnicity}
                          onSelect={(currentValue) => {
                            handleSelectChange(field.name, currentValue);
                            setEthnicityOpen(false);
                          }}
                        >
                          <Check
                            className={cn(
                              "mr-2 h-4 w-4",
                              formData[field.name] === ethnicity ? "opacity-100" : "opacity-0"
                            )}
                          />
                          {ethnicity}
                        </CommandItem>
                      ))}
                    </CommandGroup>
                  </CommandList>
                </Command>
              </PopoverContent>
            </Popover>
          ) : field.type === 'select' ? (
            <Select
              defaultValue=""
              onValueChange={(value) => handleSelectChange(field.name, value)}
            >
              <SelectTrigger className="w-full">
                <SelectValue placeholder={field.placeholder} />
              </SelectTrigger>
              <SelectContent position="popper" className="z-[9999]">
                {field.options?.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          ) : (
            <Input
              type={field.type}
              name={field.name}
              id={field.name}
              value={formData[field.name] || ''}
              onChange={handleChange}
              placeholder={field.placeholder}
              className={cn(
                "input",
                errors[field.name] && "border-red-500 focus:ring-red-500"
              )}
            />
          )}
          {errors[field.name] && (
            <p className="text-sm text-red-500 mt-1">
              {errors[field.name]}
            </p>
          )}
        </div>
      ))}

      <div className="flex gap-4 mt-6">
        <Button
          type="button"
          variant="secondary"
          className="w-full"
          onClick={toggleSelecting}
        >
          {isSelecting ? '选点模式已开启' : '地图选点'}
        </Button>

        <Button 
          type="submit" 
          variant="default"
          className="w-full"
        >
          提交
        </Button>
      </div>
      {errors.map && (
        <p className="text-sm text-red-500 mt-2 text-center">
          {errors.map}
        </p>
      )}
    </form>
  );
};

export default FormDetails;