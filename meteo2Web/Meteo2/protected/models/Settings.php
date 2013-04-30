<?php
/**
 * Created by JetBrains PhpStorm.
 * User: stavel
 * Date: 30.04.13
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
class Settings extends CActiveRecord
{

    public static function model($className=__CLASS__)
    {
        return parent::model($className);
    }

    public function tableName()
    {
        return 'tbl_post';
    }

}
