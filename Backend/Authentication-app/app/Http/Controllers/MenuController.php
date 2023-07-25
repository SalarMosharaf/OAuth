<?php

namespace App\Http\Controllers;

use App\Models\AdminMenu;
use App\Models\User;
use App\Models\UserMenu;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;



class MenuController extends Controller
{
    public function AdminMenuItems(Request $request){
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
        ]);

        if ($validator->fails()) {
            // return $this->errorResponse($validator->messages(), 422);
        }

        $items = AdminMenu::all();

            return [
                'items' => $items
            ];

    }

    public function UserMenuItems(Request $request){
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
        ]);

        if ($validator->fails()) {
            return $this->errorResponse($validator->messages(), 422);
        }


        $items = UserMenu::all();

            return $this->successResponse([
                'items' => $items
            ], 200);

    }


}
