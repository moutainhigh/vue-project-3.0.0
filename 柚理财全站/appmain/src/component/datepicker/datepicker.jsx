import React from 'react';
import ReactDOM from 'react-dom';
import { DatePicker,Table,Modal } from 'antd';
import {Publiclib} from '../../common/common';
const RangePicker = DatePicker.RangePicker;

const disabledDate = function(current) {
  // can not select days after today
  return current && current.getTime() > Date.now();
};

function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        console.log(url);
        window.location.href = url;
      }
    }
  });
}

export default class Datepicker extends React.Component{

  constructor(props) {
    super(props);
    this.state = {
      startValue: null,
      endValue: null,
      endOpen: false
    };
    this.disabledStartDate = this.disabledStartDate.bind(this);
    this.disabledEndDate = this.disabledEndDate.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onStartChange = this.onStartChange.bind(this);
    this.onEndChange = this.onEndChange.bind(this);
    this.handleStartToggle = this.handleStartToggle.bind(this);
    this.handleEndToggle = this.handleEndToggle.bind(this);
    this.startvalue = '';
    this.endvalue ='';
  }

  disabledStartDate(startValue) {
    if (!startValue || !this.state.endValue) {
      return false;
    }
    return startValue.getTime() >= this.state.endValue.getTime();
  }

  disabledEndDate(endValue) {
    if (!endValue || !this.state.startValue) {
      return false;
    }
    return endValue.getTime() <= this.state.startValue.getTime();
  }

  onChange(value) {
    this.props.callback();
    console.log('From: ', value[0], ', to: ', value[1]);
    this.startvalue = Publiclib.formatDate(value[0]);
    this.endvalue = Publiclib.formatDate(value[1]);
    //console.log(this.startvalue,this.endValue)
    this.getlist(this.props.url);
  }

  onStartChange(value) {
    this.startvalue = Publiclib.formatDate(value);
    if(this.startvalue == '1970-01-01'){
      this.startvalue = '';
    }
    this.onChange('startValue', value);
  }

  onEndChange(value) {
    this.endvalue = Publiclib.formatDate(value);
    if(this.endvalue == '1970-01-01'){
      this.endvalue = '';
    }    
    this.onChange('endValue', value);
  }

  handleStartToggle({ open }) {
    if (!open) {
      this.setState({ endOpen: true });
    }
  }

  handleEndToggle({ open }) {
    this.setState({ endOpen: open });
  }

  clean(){
    this.setState({
      startValue: null,
      endValue:null,
    });
    this.startvalue ="";
    this.endvalue ="";
  }

  returntime(){
    return[this.startvalue,this.endvalue];
  }

  getlist(urls,page){
    const that = this;
    let pages = 1;
    if(page){
      pages = page;
    }
    let datas = {dateType:"00",startTime:that.startvalue,endTime:that.endvalue,"page.page":pages,"page.pageSize":that.props.pageSize};
    if(!this.props.selectname)
    {
      datas.status = this.props.selectvalue;
    }  
    else
    {
      datas[this.props.selectname] = this.props.selectvalue;
    }    
    $.ajax({
        url: urls,
        type: 'POST',
        dataType: 'json',
        data: datas
      })
      .done(function(datas) {

        if(!datas.result)
        {
          error(datas.msg,datas.url);
          return false;
        }

        let data = datas.data; 

        if(data.rows && data.rows.length){
          $.each(data.rows,function(index, el) {
            el.key = index;
          });          
          that.setState({
            data: data.rows
          });
          const columns = that.props.columns;
          const pagination = {
            total: data.count,
            showSizeChanger: false,
            pageSize:data.pageSize,
            onChange(current) {
              that.getlist(that.props.url,current);
            },
          };          
          ReactDOM.render(<Table columns={columns} dataSource={that.state.data} pagination={pagination} />,  document.getElementById(that.props.tableid));
          if(pages === 1){
            $(".ant-table-pagination li").eq(1)[0].click();
          }                    
        }
        else
        {
          $("#"+that.props.tableid).html("<div class='nolist'><em class='iconfont'>&#xe638;</em>暂无记录</div>");
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
  }
  render() {
    return (
      <div>
        <RangePicker onChange={this.onChange} disabledDate={disabledDate}/>
      </div>
    );
  }
}